package ns.sos.domain.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ns.sos.domain.board.model.Board;
import ns.sos.domain.board.repository.BoardRepository;
import ns.sos.domain.image.model.BoardImage;
import ns.sos.domain.image.model.dto.request.BoardImageRequest;
import ns.sos.domain.image.model.dto.response.ImageResponse;
import ns.sos.domain.image.model.dto.response.ImageResponses;
import ns.sos.domain.image.repository.BoardImageRepository;
import ns.sos.global.error.BusinessException;
import ns.sos.global.error.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageService {

    private final AmazonS3 amazonS3;
    private final BoardImageRepository boardImageRepository;
    private final BoardRepository boardRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String upload(MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new BusinessException(ErrorCode.EMPTY_FILE_EXCEPTION);
        }
        return this.uploadImage(image);
    }

    private String uploadImage(MultipartFile image) {
        this.validateImageFileExtention(image.getOriginalFilename());
        try {
            return this.uploadImageToS3(image);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    private void validateImageFileExtention(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new BusinessException(ErrorCode.EMPTY_FILE_EXCEPTION);
        }

        String extention = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif", "mp4", "avi", "mov", "wmv", "flv", "mkv");

        if (!allowedExtentionList.contains(extention)) {
            throw new BusinessException(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest); // put image to S3
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD); // 다른에러
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteImageFromS3(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);

        BoardImage boardImage = boardImageRepository.findByAddr(imageAddress)
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST));

        boardImageRepository.delete(boardImage);

        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new BusinessException(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }
    }

    public String generatePresignedUrl(final String saveFileName) {
        return amazonS3.getUrl(bucketName, saveFileName).toString();
    }

    @Transactional
    public void saveBoardImage(final BoardImageRequest boardImageRequest) {
        Board board = boardRepository.findById(boardImageRequest.getBoardId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));

        BoardImage boardImage = BoardImage.of(board, boardImageRequest.getAddr());
        boardImageRepository.save(boardImage);
    }

    @Transactional(readOnly = true)
    public ImageResponses getBoardImage(Integer boardId) {
        List<BoardImage> boardImages = boardImageRepository.findByBoardId(boardId);

        List<ImageResponse> imageResponses = boardImages.stream()
                .map(boardImage -> ImageResponse.from(boardImage.getAddr()))
                .toList();

        return ImageResponses.from(imageResponses);
    }
}
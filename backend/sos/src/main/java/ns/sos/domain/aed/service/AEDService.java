package ns.sos.domain.aed.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.aed.model.AED;
import ns.sos.domain.aed.model.dto.response.AEDResponse;
import ns.sos.domain.aed.model.dto.response.AEDResponses;
import ns.sos.domain.aed.repository.AEDRepository;
import ns.sos.global.util.GeoUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AEDService {

    private final AEDRepository aedRepository;

    @Transactional(readOnly = true)
    public AEDResponses findLocationsWithinBoundingBox(final double lat, final double lon, final double radius) {
        double[] boundingBox = GeoUtil.calculateBoundingBox(lat, lon, radius);
        double minLat = boundingBox[0];
        double maxLat = boundingBox[1];
        double minLon = boundingBox[2];
        double maxLon = boundingBox[3];

        List<AED> aeds = aedRepository.findAllWithinBoundingBox(minLat, maxLat, minLon, maxLon);
        List<AEDResponse> aedResponses = aeds.stream()
                .map(AEDResponse::from)
                .toList();

        return AEDResponses.from(aedResponses);
    }

    public void insert() throws IOException {
        // 페이지 수 설정
        int totalPages = 49;

        try {
            // 각 페이지를 순회하면서 데이터 가져오기
            for (int pageNo = 1; pageNo <= totalPages; pageNo++) {
                // API URL 구성
                StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/AEDInfoInqireService/getAedFullDown");
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=0LNBDDI38IpY7O6R6QqVvIQ7RfzCly1JUfohtiLAFfzI5lNAT30cTA%2FpWT3PKkVTZLzNZV6Yvz31e8NKeR95xQ%3D%3D");
                urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                int responseCode = conn.getResponseCode();

                BufferedReader rd;
                if (responseCode >= 200 && responseCode <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();

                // XML 응답을 파싱합니다.
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document document = builder.parse(new InputSource(new StringReader(sb.toString())));

                    // item 요소를 가져옵니다.
                    NodeList itemList = document.getElementsByTagName("item");
                    for (int i = 0; i < itemList.getLength(); i++) {
                        Node itemNode = itemList.item(i);
                        if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element itemElement = (Element) itemNode;

                            // 각 태그의 값을 가져옵니다.
                            String buildAddress = getTagValue("buildAddress", itemElement);
                            String gugun = getTagValue("gugun", itemElement);
                            String sido = getTagValue("sido", itemElement);
                            String buildPlace = getTagValue("buildPlace", itemElement);
                            String clerkTel = getTagValue("clerkTel", itemElement);
                            double wgs84Lat = getTagValueAsDouble("wgs84Lat", itemElement);
                            double wgs84Lon = getTagValueAsDouble("wgs84Lon", itemElement);

                            // 요일별 시간 데이터를 추출 및 결합합니다.
                            String monTime = getTagValue("monSttTme", itemElement) + getTagValue("monEndTme", itemElement);
                            String tueTime = getTagValue("tueSttTme", itemElement) + getTagValue("tueEndTme", itemElement);
                            String wedTime = getTagValue("wedSttTme", itemElement) + getTagValue("wedEndTme", itemElement);
                            String thuTime = getTagValue("thuSttTme", itemElement) + getTagValue("thuEndTme", itemElement);
                            String friTime = getTagValue("friSttTme", itemElement) + getTagValue("friEndTme", itemElement);
                            String holTime = getTagValue("holSttTme", itemElement) + getTagValue("holEndTme", itemElement);

                            aedRepository.save(new AED(sido, gugun, buildAddress, buildPlace, wgs84Lat, wgs84Lon, clerkTel,monTime, tueTime, wedTime, thuTime, friTime, holTime));
                        }
                    }

                } catch (ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // XML 요소에서 특정 태그의 값을 가져오는 메서드입니다.
    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                return node.getTextContent();
            }
        }
        return "N/A"; // 태그가 존재하지 않을 경우 기본 값을 반환합니다.
    }

    // XML 요소에서 특정 태그의 값을 double로 가져오는 메서드입니다.
    private static double getTagValueAsDouble(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                try {
                    return Double.parseDouble(node.getTextContent());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return Double.NaN; // 태그가 존재하지 않거나 변환할 수 없을 경우 기본 값을 반환합니다.
    }

}


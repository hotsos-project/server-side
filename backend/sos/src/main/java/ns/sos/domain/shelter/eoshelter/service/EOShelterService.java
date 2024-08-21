package ns.sos.domain.shelter.eoshelter.service;

import lombok.RequiredArgsConstructor;
import ns.sos.domain.shelter.eoshelter.model.EOShelter;
import ns.sos.domain.shelter.eoshelter.model.dto.response.EOShelterResponse;
import ns.sos.domain.shelter.eoshelter.model.dto.response.EOShelterResponses;
import ns.sos.domain.shelter.eoshelter.repository.EOShelterRepository;
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
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EOShelterService {

    private final EOShelterRepository repository;

    public EOShelterResponses findSheltersWithinRadius(double lat, double lon, double radius) {

        double[] boundingBox = GeoUtil.calculateBoundingBox(lat, lon, radius);
        double minLat = boundingBox[0];
        double maxLat = boundingBox[1];
        double minLon = boundingBox[2];
        double maxLon = boundingBox[3];

        List<EOShelter> eoShelters = repository.findAllWithinBoundingBox(minLat, maxLat, minLon, maxLon);
        List<EOShelterResponse> eoShelterResponses = eoShelters.stream()
                .map(EOShelterResponse::from)
                .toList();

        return EOShelterResponses.from(eoShelterResponses);
    }

    public void insert() {
        // 페이지 수 설정
        int totalPages = 12;

        try {
            // 각 페이지를 순회하면서 데이터 가져오기
            for (int pageNo = 1; pageNo <= totalPages; pageNo++) {
                // API URL 구성
                StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1741000/EmergencyAssemblyArea_Earthquake5/getArea4List2?serviceKey=vFoZ10ER95mdfhAEIFl4R%2BITsC%2Fg7ihtmqOODO%2FhzGkMlj8nIBhnS9n033SJC98OzsfMCR0YKkDYd7%2BjMx0wQA%3D%3D&pageNo=" + pageNo + "&numOfRows=1000&type=xml");


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
                    NodeList itemList = document.getElementsByTagName("row");
                    for (int i = 0; i < itemList.getLength(); i++) {
                        Node itemNode = itemList.item(i);
                        if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element itemElement = (Element) itemNode;

                            // 각 태그의 값을 가져옵니다.
                            String sidoName = getTagValue("ctprvn_nm", itemElement); //
                            String sigunguName = getTagValue("sgg_nm", itemElement); //
                            double lon = getTagValueAsDouble("xcord", itemElement);  //
                            double lat = getTagValueAsDouble("ycord", itemElement);
                            String newAddress = getTagValue("dtl_adres", itemElement); //
                            String name = getTagValue("vt_acmdfclty_nm", itemElement); //
                            String tel = getTagValue("mngps_telno", itemElement); //

                            repository.save(new EOShelter(sidoName, sigunguName, newAddress, name, lat, lon, tel));
                        }
                    }

                } catch (ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (ProtocolException e) {
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


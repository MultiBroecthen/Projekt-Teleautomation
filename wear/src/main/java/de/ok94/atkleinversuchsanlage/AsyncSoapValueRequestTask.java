package de.ok94.atkleinversuchsanlage;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class AsyncSoapValueRequestTask extends AsyncTask<Void, Void, Void> {

    private static final String SOAP_ACTION = "\"http://opcfoundation.org/webservices/XMLDA/1.0/Read\"";
    private static final String SOAP_URL = "http://141.30.154.211:8087/OPC/DA";
    private static final String SOAP_MESSAGE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<SOAP-ENV:Envelope\n" +
            "    xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
            "    xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
            "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "    <SOAP-ENV:Body>\n" +
            "        <m:Read xmlns:m=\"http://opcfoundation.org/webservices/XMLDA/1.0/\">\n" +
            "            <m:Options ReturnErrorText=\"false\" ReturnDiagnosticInfo=\"false\" ReturnItemTime=\"false\" ReturnItemPath=\"false\" ReturnItemName=\"true\"/>\n" +
            "            <m:ItemList>\n" +
            "                <m:Items ItemName=\"Schneider/Fuellstand1_Ist\"/>\n" +
            "                <m:Items ItemName=\"Schneider/Fuellstand2_Ist\"/>\n" +
            "                <m:Items ItemName=\"Schneider/Fuellstand3_Ist\"/>\n" +
            "                <m:Items ItemName=\"Schneider/LH1\"/>\n" +
            "                <m:Items ItemName=\"Schneider/LH2\"/>\n" +
            "                <m:Items ItemName=\"Schneider/LH3\"/>\n" +
            "                <m:Items ItemName=\"Schneider/LL1\"/>\n" +
            "                <m:Items ItemName=\"Schneider/LL2\"/>\n" +
            "                <m:Items ItemName=\"Schneider/LL3\"/>\n" +
            "            </m:ItemList>\n" +
            "        </m:Read>\n" +
            "    </SOAP-ENV:Body>\n" +
            "</SOAP-ENV:Envelope>";
    private static final String XPATH_LEVEL1 = "/Envelope/Body/ReadResponse/RItemList/Items[@ItemName='Schneider/Fuellstand1_Ist']/Value";
    private static final String XPATH_LEVEL2 = "/Envelope/Body/ReadResponse/RItemList/Items[@ItemName='Schneider/Fuellstand2_Ist']/Value";
    private static final String XPATH_LEVEL3 = "/Envelope/Body/ReadResponse/RItemList/Items[@ItemName='Schneider/Fuellstand3_Ist']/Value";
    private static final String XPATH_LL1 = "/Envelope/Body/ReadResponse/RItemList/Items[@ItemName='Schneider/LL1']/Value";
    private static final String XPATH_LL2 = "/Envelope/Body/ReadResponse/RItemList/Items[@ItemName='Schneider/LL2']/Value";
    private static final String XPATH_LL3 = "/Envelope/Body/ReadResponse/RItemList/Items[@ItemName='Schneider/LL3']/Value";
    private static final String XPATH_LH1 = "/Envelope/Body/ReadResponse/RItemList/Items[@ItemName='Schneider/LH1']/Value";
    private static final String XPATH_LH2 = "/Envelope/Body/ReadResponse/RItemList/Items[@ItemName='Schneider/LH2']/Value";
    private static final String XPATH_LH3 = "/Envelope/Body/ReadResponse/RItemList/Items[@ItemName='Schneider/LH3']/Value";

    private OnValuesAvailable listener;

    private float level1, level2, level3;
    private boolean ll1, ll2, ll3, lh1, lh2, lh3;

    AsyncSoapValueRequestTask(Context context) {
        listener = (OnValuesAvailable) context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String soapResponse = sendSoapReadRequest(SOAP_MESSAGE);
        Log.d("SOAP_RESPONSE", soapResponse);

        readSoapReadResponse(soapResponse);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.setTankLevels(level1, level2, level3);
        listener.setCapacitiveSensorStates(ll1, ll2, ll3, lh1, lh2, lh3);
    }

    private String sendSoapReadRequest(String soapMessage) {
        URL url;
        HttpURLConnection connection = null;
        String soapResponse = "";
        try {
            url = new URL(SOAP_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("SOAPAction", SOAP_ACTION);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(soapMessage);
            dataOutputStream.flush();
            dataOutputStream.close();

            InputStream inputStream;
            if (connection.getResponseCode() <= 400) {
                inputStream = connection.getInputStream();
            }
            else {
                inputStream = connection.getErrorStream();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }
            bufferedReader.close();
            soapResponse = response.toString();
        }
        catch (Exception e) {
            Log.e("CONNECTION", e.toString());
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return soapResponse;
    }

    private void readSoapReadResponse(String soapResponse) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(soapResponse.getBytes(StandardCharsets.UTF_8.name())));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();

            XPathExpression expr = xpath.compile(XPATH_LEVEL1);
            level1 = (float) ((double) expr.evaluate(doc, XPathConstants.NUMBER));
            expr = xpath.compile(XPATH_LEVEL2);
            level2 = (float) ((double) expr.evaluate(doc, XPathConstants.NUMBER));
            expr = xpath.compile(XPATH_LEVEL3);
            level3 = (float) ((double) expr.evaluate(doc, XPathConstants.NUMBER));

            expr = xpath.compile(XPATH_LL1);
            ll1 = Boolean.parseBoolean((String) expr.evaluate(doc, XPathConstants.STRING));
            expr = xpath.compile(XPATH_LL2);
            ll2 = Boolean.parseBoolean((String) expr.evaluate(doc, XPathConstants.STRING));
            expr = xpath.compile(XPATH_LL3);
            ll3 = Boolean.parseBoolean((String) expr.evaluate(doc, XPathConstants.STRING));
            expr = xpath.compile(XPATH_LH1);
            lh1 = Boolean.parseBoolean((String) expr.evaluate(doc, XPathConstants.STRING));
            expr = xpath.compile(XPATH_LH2);
            lh2 = Boolean.parseBoolean((String) expr.evaluate(doc, XPathConstants.STRING));
            expr = xpath.compile(XPATH_LH3);
            lh3 = Boolean.parseBoolean((String) expr.evaluate(doc, XPathConstants.STRING));
        }
        catch (Exception e) {
            Log.e("XML_PARSE", e.toString());
        }
    }

    public interface OnValuesAvailable {
        void setTankLevels(float level1, float level2, float level3);

        void setCapacitiveSensorStates(boolean ll1, boolean ll2, boolean ll3, boolean lh1, boolean lh2, boolean lh3);
    }
}

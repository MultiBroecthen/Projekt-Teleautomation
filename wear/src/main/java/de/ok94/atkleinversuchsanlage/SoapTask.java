package de.ok94.atkleinversuchsanlage;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class SoapTask extends AsyncTask<Void, Void, Void> {

    private static final String SOAP_ACTION = "\"http://opcfoundation.org/webservices/XMLDA/1.0/Read\"";
    private static final String SOAP_URL = "http://141.30.154.211:8087/OPC/DA";
    private final String SOAP_REQUEST;

    SoapTask(String soapRequest) {
        SOAP_REQUEST = soapRequest;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String soapResponse = sendSoapRequest(SOAP_REQUEST);

        readSoapResponse(soapResponse);

        return null;
    }

    private String sendSoapRequest(String soapRequest) {
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
            dataOutputStream.writeBytes(soapRequest);
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
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append('\n');
            }
            bufferedReader.close();
            soapResponse = responseBuilder.toString();
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

    protected abstract void readSoapResponse(String soapResponse);
}

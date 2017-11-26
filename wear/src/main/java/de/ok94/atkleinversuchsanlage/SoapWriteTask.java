package de.ok94.atkleinversuchsanlage;

import android.util.Log;


public class SoapWriteTask extends SoapTask {

    private static final String SOAP_ACTION = "\"http://opcfoundation.org/webservices/XMLDA/1.0/Write\"";
    private static final String STOP_PUMP_REQUEST = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
            "                   xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" \n" +
            "                   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
            "                   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <SOAP-ENV:Body>\n" +
            "    <m:Write xmlns:m=\"http://opcfoundation.org/webservices/XMLDA/1.0/\">\n" +
            "      <m:Options ReturnErrorText=\"true\" ReturnDiagnosticInfo=\"true\" ReturnItemTime=\"true\" ReturnItemPath=\"true\" ReturnItemName=\"true\"/>\n" +
            "      <m:ItemList>\n" +
            "        <m:Items ItemName=\"Schneider/Start_Umpumpen_FL\">\n" +
            "		   <m:Value xsi:type=\"xsd:boolean\">false</m:Value>\n" +
            "		 </m:Items>\n" +
            "      </m:ItemList>\n" +
            "    </m:Write>\n" +
            "  </SOAP-ENV:Body>\n" +
            "</SOAP-ENV:Envelope>";
    private static final String START_PUMP_REQUEST = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
            "                   xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" \n" +
            "                   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
            "                   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
            "  <SOAP-ENV:Body>\n" +
            "    <m:Write xmlns:m=\"http://opcfoundation.org/webservices/XMLDA/1.0/\">\n" +
            "      <m:Options ReturnErrorText=\"true\" ReturnDiagnosticInfo=\"true\" ReturnItemTime=\"true\" ReturnItemPath=\"true\" ReturnItemName=\"true\"/>\n" +
            "      <m:ItemList>\n" +
            "        <m:Items ItemName=\"Schneider/Behaelter_A_FL\">\n" +
            "		   <m:Value xsi:type=\"xsd:int\">%1$d</m:Value>\n" +
            "		 </m:Items>\n" +
            "        <m:Items ItemName=\"Schneider/Behaelter_B_FL\">\n" +
            "		   <m:Value xsi:type=\"xsd:int\">%2$d</m:Value>\n" +
            "		 </m:Items>\n" +
            "        <m:Items ItemName=\"Schneider/Start_Umpumpen_FL\">\n" +
            "		   <m:Value xsi:type=\"xsd:boolean\">true</m:Value>\n" +
            "		 </m:Items>\n" +
            "      </m:ItemList>\n" +
            "    </m:Write>\n" +
            "  </SOAP-ENV:Body>\n" +
            "</SOAP-ENV:Envelope>";

    SoapWriteTask(boolean startPumping, int tankA, int tankB) {
        super(SOAP_ACTION, (startPumping ? String.format(START_PUMP_REQUEST, tankA, tankB) : STOP_PUMP_REQUEST));
        Log.d("WRITE_REQUEST", (startPumping ? String.format(START_PUMP_REQUEST, tankA, tankB) : STOP_PUMP_REQUEST));
    }

    @Override
    protected void readSoapResponse(String soapResponse) {
        Log.i("WRITE_RESPONSE", soapResponse);
    }
}

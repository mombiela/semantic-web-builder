package org.swb.processor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.util.encoders.Base64;
import org.swb.Processor;

public class PayPal implements Processor
{
    private String name = "paypal";
    
    private String keyPath;
    private String certPath;
    private String paypalCertPath;
    private String keyPass;    
    
    @Override
    public void init(String name, Properties config) throws Exception
    {
        this.name = name;
        if (config.containsKey("out")) this.name = config.getProperty("out");
        
        keyPath = config.getProperty("key_path");
        certPath = config.getProperty("cert_path");
        paypalCertPath = config.getProperty("paypal_cert_path");
        keyPass = config.getProperty("key_password");
    }

    @Override
    public void execute(Map<String, Object> context) throws Exception
    {
        context.put(name, this);
    }
    
    public static void main(String[] args) throws Exception
    {
        System.out.println("Inici");
        
        // Creamos properties
        Properties props = new Properties();
        props.setProperty("key_path", "paypal/prvkey.p12");
        props.setProperty("cert_path", "paypal/pubcert.pem");
        props.setProperty("paypal_cert_path", "paypal/paypal_cert_pem.txt");
        props.setProperty("key_password", "123456");
        
        // Creamos objeto
        PayPal pp = new PayPal();
        pp.init("papypal", props);
        
        // Creamos resultado
        String result = pp.encrypt("cmd=_xclick|business=webmaster@maregaia.es|amount=23.99|currency_code=EUR|item_name=todos son asi|cert_id=CR4H75VZVVBJW|cancel_return=http://test.maregaia.es/es/comprar.html",
                "http://test.maregaia.es/images/comprar.png",
                "Comprar", null);
        result += "\n\n";
        result += pp.encrypt("cmd=_cart|add=1|shopping_url=http://test.maregaia.es/es/comprar.html|business=webmaster@maregaia.es|amount=23.99|currency_code=EUR|item_name=Todos son asi, cierto|cert_id=CR4H75VZVVBJW|cancel_return=http://test.maregaia.es/es/comprar.html",
                "http://test.maregaia.es/images/comprar.png",
                "Añadir al carro", "blank");                
        
        FileUtils.writeStringToFile(new File("paypal/demo.html"), result, "ISO-8859-1");
        
        System.out.println("Fi");
    }
    
    // -----------------------------
    // Métodos principales de paypal
    // -----------------------------
    
    public String encrypt(String cmdText, String image, String altText, String target) throws Exception 
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 

        String result = getButtonEncryptionValue( cmdText, keyPath, certPath, paypalCertPath, keyPass );
            
        StringBuffer out = new StringBuffer();
      
        out.append( "<form action=\"https://www.paypal.com/cgi-bin/webscr\" ");
        if (target != null && !target.trim().equals(""))
        {
            out.append("target=\"" + target + "\"" );
        }
        out.append("method=\"post\">" );
        out.append( "<input type=\"hidden\" name=\"cmd\" value=\"_s-xclick\">" );  ;
        out.append( "<input type=\"image\" src=\"" + image + "\" border=\"0\" name=\"submit\" " );
        out.append( "alt=\"" + altText + "\" title=\"" + altText + "\">" );
        out.append( "<input type=\"hidden\" name=\"encrypted\" value=\"" );
        out.append( result );
        out.append( "\">" );
        out.append( "</form>");
       
        return out.toString();
    }
    
    // ------------------------
    // Encryption (Paypal code)
    // ------------------------
    
    private static String getButtonEncryptionValue(String _data, String _privateKeyPath, String _certPath, String _payPalCertPath, String _keyPass) throws Exception
    {
        _data = _data.replace('|', '\n');
        CertificateFactory cf = CertificateFactory.getInstance("X509", "BC");

        // Read the Private Key
        KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
        ks.load(new FileInputStream(_privateKeyPath), _keyPass.toCharArray());

        String keyAlias = null;
        Enumeration aliases = ks.aliases();
        while (aliases.hasMoreElements())
        {
            keyAlias = (String) aliases.nextElement();
        }

        PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, _keyPass.toCharArray());

        // Read the Certificate
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(new FileInputStream(_certPath));

        // Read the PayPal Cert
        X509Certificate payPalCert = (X509Certificate) cf.generateCertificate(new FileInputStream(_payPalCertPath));

        // Create the Data
        byte[] data = _data.getBytes();

        // Sign the Data with my signing only key pair
        CMSSignedDataGenerator signedGenerator = new CMSSignedDataGenerator();

        signedGenerator.addSigner(privateKey, certificate, CMSSignedDataGenerator.DIGEST_SHA1);

        ArrayList certList = new ArrayList();
        certList.add(certificate);
        CertStore certStore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList));
        signedGenerator.addCertificatesAndCRLs(certStore);

        CMSProcessableByteArray cmsByteArray = new CMSProcessableByteArray(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cmsByteArray.write(baos);
        System.out.println("CMSProcessableByteArray contains [" + baos.toString() + "]");

        CMSSignedData signedData = signedGenerator.generate(cmsByteArray, true, "BC");

        byte[] signed = signedData.getEncoded();

        CMSEnvelopedDataGenerator envGenerator = new CMSEnvelopedDataGenerator();
        envGenerator.addKeyTransRecipient(payPalCert);
        CMSEnvelopedData envData = envGenerator.generate(new CMSProcessableByteArray(signed), CMSEnvelopedDataGenerator.DES_EDE3_CBC, "BC");

        byte[] pkcs7Bytes = envData.getEncoded();

        return new String(DERtoPEM(pkcs7Bytes, "PKCS7"));

    }

    private static byte[] DERtoPEM(byte[] bytes, String headfoot)
    {
        ByteArrayOutputStream pemStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(pemStream);

        byte[] stringBytes = Base64.encode(bytes);

        System.out.println("Converting " + stringBytes.length + " bytes");

        String encoded = new String(stringBytes);

        if (headfoot != null)
        {
            writer.print("-----BEGIN " + headfoot + "-----\n");
        }

        // write 64 chars per line till done
        int i = 0;
        while ((i + 1) * 64 < encoded.length())
        {
            writer.print(encoded.substring(i * 64, (i + 1) * 64));
            writer.print("\n");
            i++;
        }
        if (encoded.length() % 64 != 0)
        {
            writer.print(encoded.substring(i * 64)); // write remainder
            writer.print("\n");
        }
        if (headfoot != null)
        {
            writer.print("-----END " + headfoot + "-----\n");
        }
        writer.flush();
        return pemStream.toByteArray();
    }
    
}

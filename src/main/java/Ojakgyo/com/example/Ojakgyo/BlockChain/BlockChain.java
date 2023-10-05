/**
 * 하이퍼레저 1.4.1 네이버 클라우드 버전
 */

package Ojakgyo.com.example.Ojakgyo.BlockChain;


import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import Ojakgyo.com.example.Ojakgyo.dto.BlockChainContract;
import org.codehaus.jackson.map.ObjectMapper;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;

@Service
public class BlockChain {

    private final ObjectMapper mapper = new ObjectMapper();

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");
    }

    // helper function for getting connected to the gateway
    public static Gateway connect() throws Exception{
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallet.createFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\resources\\connection\\connection_property_organization-msp.json");
        InputStream is = new FileInputStream(networkConfigPath.toFile());
        NetworkConfig ccp = NetworkConfig.fromJsonStream(is);
        String mspId = ccp.getClientOrganization().getMspId();

        // load the exported user
        // load the exported user, 똑같이 냅둬도 됨
        Path userPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\resources\\connection\\ca-user.json");
        is = new FileInputStream(userPath.toFile());
        JsonObject userObject = (JsonObject) Json.createReader(is).read();
        String userId = userObject.getString("name");

        boolean userExists = wallet.exists(userId);
        if (!userExists) {
            CryptoPrimitives crypto = new CryptoPrimitives();
            Wallet.Identity user = Wallet.Identity.createIdentity(mspId,
                    new String(Base64.getDecoder().decode(userObject.getString("cert"))),
                    crypto.bytesToPrivateKey(Base64.getDecoder().decode(userObject.getString("key"))));
            wallet.put(userId, user);
        }


        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, userId)
                .networkConfig(networkConfigPath)
                .discovery(true);

        return builder.connect();
    }

    public BlockChainContract getContract(Long dealId) throws Exception {
        // create a gateway connection
        BlockChainContract blockChainContract;
        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("channel1");
            Contract contract = network.getContract("Ojakgyocc");

            byte[] result = contract.evaluateTransaction("ReadAsset", String.valueOf(dealId));
            blockChainContract = mapper.readValue(result, BlockChainContract.class);
            System.exit(0);
        }

        return blockChainContract;
    }


    public void createContract(BlockChainContract blockChainContract) throws Exception {
        try (Gateway gateway = connect()) {
            // get the network and contract
            Network network = gateway.getNetwork("channel1");
            Contract contract = network.getContract("Ojakgyocc");

            byte[] result = contract.evaluateTransaction("CreateAsset", String.valueOf(blockChainContract.getDealId())
                    ,blockChainContract.getRepAndRes(), blockChainContract.getNote(), String.valueOf(blockChainContract.getPrice()));
            System.out.println(new String(result));
            System.exit(0);
        }
    }

}

/**
 *
 *  * implementation 'org.hyperledger.fabric:fabric-gateway-java:2.1.1'
 *  * 개쌉짓거리 전
 *
 */
//
//package Ojakgyo.com.example.Ojakgyo.BlockChain;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Base64;
//import java.util.Properties;
//
//import Ojakgyo.com.example.Ojakgyo.dto.BlockChainContract;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.hyperledger.fabric.gateway.*;
//import org.hyperledger.fabric.protos.msp.Identities;
//import org.hyperledger.fabric.sdk.Enrollment;
//import org.hyperledger.fabric.sdk.NetworkConfig;
//import org.hyperledger.fabric.sdk.identity.Identity;
//import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
//import org.hyperledger.fabric.sdk.security.CryptoSuite;
//import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
//import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
//import org.hyperledger.fabric_ca.sdk.HFCAClient;
//import org.springframework.stereotype.Service;
//
//import javax.json.Json;
//import javax.json.JsonObject;
//
//@Service
//public class BlockChain {
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    static {
//        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");
//    }
//
//    // helper function for getting connected to the gateway
//    public static Gateway connect() throws Exception{
//        // Load a file system based wallet for managing identities.
//        Path walletPath = Paths.get("wallet");
//        Wallet wallet = Wallet.createFileSystemWallet(walletPath);
//        // load a CCP
//        Path networkConfigPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\resources\\connection\\connection_profile.json");
//        InputStream is = new FileInputStream(networkConfigPath.toFile());
//        NetworkConfig ccp = NetworkConfig.fromJsonStream(is);
//        String mspId = ccp.getClientOrganization().getMspId();
//
//        // load the exported user
//        // load the exported user, 똑같이 냅둬도 됨
//        Path userPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\resources\\connection\\user.json");
//        is = new FileInputStream(userPath.toFile());
//        JsonObject userObject = (JsonObject) Json.createReader(is).read();
//        String userId = userObject.getString("name");
//
//
//        // Create a CA client for interacting with the CA.
//        if (wallet.get(userId) == null) {
////            Properties props = new Properties();
////            props.put("pemFile",
////                    "C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\resources\\connection\\ojakgyo-new-key.pem");
////            props.put("allowAllHostNames", "true");
////            HFCAClient caClient = HFCAClient.createNewInstance("grpcs://organization-ca-hrcdcf-hlf-ca.bc-wyx78myn.kr.blockchain.naverncp.com", props);
////            CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
////            caClient.setCryptoSuite(cryptoSuite);
////
////            // Enroll the admin user, and import the new identity into the wallet.
////            final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
////            enrollmentRequestTLS.addHost("localhost");
////            enrollmentRequestTLS.setProfile("tls");
////            Enrollment enrollment = caClient.enroll(userId, new String(Base64.getDecoder().decode(userObject.getString("cert"))), enrollmentRequestTLS);
////            Identity user = Identities.newX509Identity(mspId, enrollment);
////            wallet.put(userId, user);
//            CryptoPrimitives crypto = new CryptoPrimitives();
//            Wallet.Identity user = Wallet.Identity.createIdentity(mspId,
//                    new String(Base64.getDecoder().decode(userObject.getString("cert"))),
//                    crypto.bytesToPrivateKey(Base64.getDecoder().decode(userObject.getString("key"))));
//            wallet.put(userId, user);
//        }
//
//        Gateway.Builder builder = Gateway.createBuilder()
//                .identity(wallet, userId)
//                .networkConfig(networkConfigPath)
//                .discovery(true);
//
//        return builder.connect();
//    }
//
//    public BlockChainContract getContract(Long dealId) throws Exception {
//        // create a gateway connection
//        BlockChainContract blockChainContract;
//        try (Gateway gateway = connect()) {
//
//            // get the network and contract
//            Network network = gateway.getNetwork("channel1");
//            org.hyperledger.fabric.gateway.Contract contract = network.getContract("ojakgyocc");
//
//            byte[] result = contract.evaluateTransaction("ReadAsset", String.valueOf(dealId));
//            blockChainContract = mapper.readValue(result, BlockChainContract.class);
//            System.exit(0);
//        }
//
//        return blockChainContract;
//    }
//
//
//    public void createContract(BlockChainContract blockChainContract) throws Exception {
//        try (Gateway gateway = connect()) {
//
//            // get the network and contract
//            Network network = gateway.getNetwork("channel1");
//            org.hyperledger.fabric.gateway.Contract contract = network.getContract("Ojakgyocc");
//
//            byte[] result = contract.evaluateTransaction("CreateAsset", String.valueOf(blockChainContract.getDealId())
//                    ,blockChainContract.getRepAndRes(), blockChainContract.getNote());
//            System.out.println(new String(result));
//            System.exit(0);
//        }
//    }
//
//}

/**
 * implementation 'org.hyperledger.fabric:fabric-gateway-java:2.1.1'
 * 개쌉짓거리
 */
//package Ojakgyo.com.example.Ojakgyo.BlockChain;
//
//import Ojakgyo.com.example.Ojakgyo.dto.BlockChainContract;
//import org.bouncycastle.asn1.ASN1Sequence;
//import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
//import org.bouncycastle.cert.X509CertificateHolder;
//import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
//import org.bouncycastle.cms.CMSSignedData;
//import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
//import org.bouncycastle.util.Store;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.hyperledger.fabric.gateway.*;
//import org.hyperledger.fabric.sdk.Enrollment;
//import org.hyperledger.fabric.sdk.NetworkConfig;
//import org.hyperledger.fabric.sdk.identity.X509Enrollment;
//import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
//import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
//import org.hyperledger.fabric_ca.sdk.HFCAClient;
//import org.springframework.stereotype.Service;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;
//import java.security.interfaces.RSAPrivateKey;
//import java.util.Base64;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Properties;
//
//import org.hyperledger.fabric.gateway.Wallet;
//import org.hyperledger.fabric.gateway.Wallets;
//import org.hyperledger.fabric.gateway.Identities;
//import org.hyperledger.fabric.sdk.Enrollment;
//import org.hyperledger.fabric.sdk.security.CryptoSuite;
//import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
//import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
//import org.hyperledger.fabric_ca.sdk.HFCAClient;
//
//import javax.json.Json;
//import javax.json.JsonObject;
//
//
//@Service
//public class BlockChain {
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    static {
//        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "false");
//    }
//
//    // helper function for getting connected to the gateway
//    public static Gateway connect() throws Exception{
//        // Load a file system based wallet for managing identities.
//        Path walletPath = Paths.get("wallet");
//        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
//        // load a CCP
//        Path networkConfigPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\resources\\connection\\connection_profile.json");
//        InputStream is = new FileInputStream(networkConfigPath.toFile());
//        NetworkConfig ccp = NetworkConfig.fromJsonStream(is);
//        String mspId = ccp.getClientOrganization().getMspId();
//
//        // load the exported user
//        // load the exported user, 똑같이 냅둬도 됨
//        Path userPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\resources\\connection\\user.json");
//        is = new FileInputStream(userPath.toFile());
//        JsonObject userObject = (JsonObject) Json.createReader(is).read();
//        String userId = userObject.getString("name");
//
////        byte[] derData =  Base64.getDecoder().decode(userObject.getString("cert"));
////        ASN1Sequence ASN1 = ASN1Sequence.getInstance(derData);
////        PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(ASN1);
////        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
////        RSAPrivateKey privateKey = (RSAPrivateKey) converter.getPrivateKey(privateKeyInfo);
////
////        Store<X509CertificateHolder> certStore = cmsData.getCertificates();
////        Collection<X509CertificateHolder> allCollection = certStore.getMatches(null);
////        Iterator<X509CertificateHolder> allIt = allCollection.iterator();
////        X509CertificateHolder firstCertificateHolder = allIt.next();
////        X509Certificate issuedCertificate = new JcaX509CertificateConverter().getCertificate(firstCertificateHolder);
//
//        // Create a CA client for interacting with the CA.
//        if (wallet.get(userId) == null) {
//            byte[] derData = Base64.getDecoder().decode(userObject.getString("cert"));
//            CMSSignedData cmsData = new CMSSignedData(derData);
//            Store<X509CertificateHolder> certStore = cmsData.getCertificates();
//            Collection<X509CertificateHolder> allCollection = certStore.getMatches(null);
//            Iterator<X509CertificateHolder> allIt = allCollection.iterator();
//            X509CertificateHolder firstCertificateHolder = allIt.next();
//
//            X509Certificate cert = new    JcaX509CertificateConverter().getCertificate(firstCertificateHolder);
//
//
//            CryptoPrimitives crypto = new CryptoPrimitives();
//            Identity user = Identities.newX509Identity(mspId,
//                cert,
//                crypto.bytesToPrivateKey(Base64.getDecoder().decode(userObject.getString("key"))));
//
//            wallet.put(userId, user);
//        }
//
//        Gateway.Builder builder = Gateway.createBuilder()
//                .identity(wallet, userId)
//                .networkConfig(networkConfigPath)
//                .discovery(true);
//
//        return builder.connect();
//    }
//
//    public BlockChainContract getContract(Long dealId) throws Exception {
//        // create a gateway connection
//        BlockChainContract blockChainContract;
//        try (Gateway gateway = connect()) {
//
//            // get the network and contract
//            Network network = gateway.getNetwork("channel1");
//            org.hyperledger.fabric.gateway.Contract contract = network.getContract("ojakgyocc");
//
//            byte[] result = contract.evaluateTransaction("ReadAsset", String.valueOf(dealId));
//            blockChainContract = mapper.readValue(result, BlockChainContract.class);
//            System.exit(0);
//        }
//
//        return blockChainContract;
//    }
//
//
//    public void createContract(BlockChainContract blockChainContract) throws Exception {
//        try (Gateway gateway = connect()) {
//
//            // get the network and contract
//            Network network = gateway.getNetwork("channel1");
//            org.hyperledger.fabric.gateway.Contract contract = network.getContract("Ojakgyocc");
//
//            byte[] result = contract.evaluateTransaction("CreateAsset", String.valueOf(blockChainContract.getDealId())
//                    ,blockChainContract.getRepAndRes(), blockChainContract.getNote());
//            System.out.println(new String(result));
//            System.exit(0);
//        }
//    }
//
//}
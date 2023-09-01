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

    public BlockChainContract getContract(Long dealId) throws Exception {
        /** 블록체인 네트워크 연결 */
        // ID를 관리하기 위한 파일 시스템 기반 wallet을 로드
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallet.createFileSystemWallet(walletPath);

        // load a CCP, 이때 Paths.get 경로를 내가 그 다운받은 json 경로로 해줘야 할듯
        Path networkConfigPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\java\\Ojakgyo\\com\\example\\Ojakgyo\\BlockChain\\connection\\connection_profile.json");
        InputStream is = new FileInputStream(networkConfigPath.toFile());
        NetworkConfig ccp = NetworkConfig.fromJsonStream(is);
        String mspId = ccp.getClientOrganization().getMspId();

        // load the exported user, 똑같이 냅둬도 됨
        Path userPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\java\\Ojakgyo\\com\\example\\Ojakgyo\\BlockChain\\connection\\user.json");
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

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userId).networkConfig(networkConfigPath).discovery(true);

        /** 체인코드 불러오기*/
        // create a gateway connection
        BlockChainContract blockChainContract;
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("Ojakgyo");
            org.hyperledger.fabric.gateway.Contract contract = network.getContract("ojakgyocc");

            byte[] result;

            //계약서 가져오기
            result = contract.evaluateTransaction("ReadAsset", String.valueOf(dealId));
            blockChainContract = mapper.readValue(result, BlockChainContract.class);
            System.exit(0);
        }

        return blockChainContract;
    }


    public void createContract(BlockChainContract blockChainContract) throws Exception {
        /** 블록체인 네트워크 연결 */
        // ID를 관리하기 위한 파일 시스템 기반 wallet을 로드
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallet.createFileSystemWallet(walletPath);

        // load a CCP, 이때 Paths.get 경로를 내가 그 다운받은 json 경로로 해줘야 할듯
        Path networkConfigPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\java\\Ojakgyo\\com\\example\\Ojakgyo\\BlockChain\\connection\\connection_profile.json");
        InputStream is = new FileInputStream(networkConfigPath.toFile());
        NetworkConfig ccp = NetworkConfig.fromJsonStream(is);
        String mspId = ccp.getClientOrganization().getMspId();

        // load the exported user, 똑같이 냅둬도 됨
        Path userPath = Paths.get("C:\\Users\\a0107\\IdeaProjects\\Ojakgyo\\src\\main\\java\\Ojakgyo\\com\\example\\Ojakgyo\\BlockChain\\connection\\user.json");
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

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userId).networkConfig(networkConfigPath).discovery(true);

        /** 체인코드 불러오기*/
        // create a gateway connectiond
        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("Ojakgyo");
            org.hyperledger.fabric.gateway.Contract contract = network.getContract("ojakgyocc");

            byte[] result;
            //계약서 가져오기
            result = contract.evaluateTransaction("CreateAsset", String.valueOf(blockChainContract.getDealId())
                                ,blockChainContract.getRepAndRes(), blockChainContract.getNote());
            System.out.println(new String(result));
            System.exit(0);
        }
    }

}
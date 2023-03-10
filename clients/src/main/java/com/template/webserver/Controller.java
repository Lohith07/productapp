package com.template.webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.flows.CreateProductFlow;
import com.template.flows.TransferProductFlow;
import com.template.flows.UpdateProductFlow;
import com.template.states.ProductState;

import co.paralleluniverse.strands.channels.Port;
import net.corda.client.jackson.JacksonSupport;
import net.corda.client.rpc.CordaRPCClient;
import net.corda.client.rpc.CordaRPCConnection;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.CordaX500Name;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.node.NodeInfo;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.NetworkHostAndPort;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
public class Controller {
    private final CordaRPCOps proxy;
    private final CordaX500Name me;
    private CordaRPCConnection rpcConnection;

    private final static Logger logger = LoggerFactory.getLogger(Controller.class);

    public Controller(NodeRPCConnection rpc) {
        this.proxy = rpc.proxy;
        this.me = proxy.nodeInfo().getLegalIdentities().get(0).getName();
    }

    @Value("${config.rpc.host}")
    private String host;
    // The RPC port of the node we are connecting to.
    @Value("${config.rpc.username}")
    private String username;
    // The username for logging into the RPC client.
    @Value("${config.rpc.password}")
    private String password;
    // The password for logging into the RPC client.
    @Value("${config.rpc.port}")
    private String rpcPort;

    @Value("${server.port}")
    private String serverPort;
    // NetworkHostAndPort rpcAddress = new NetworkHostAndPort(host, rpcPort);
    // CordaRPCClient rpcClient = new CordaRPCClient(rpcAddress);


    public String toDisplayString(X500Name name) {
        return BCStyle.INSTANCE.toString(name);
    }

    private boolean isNotary(NodeInfo nodeInfo) {
        return !proxy.notaryIdentities()
                .stream().filter(el -> nodeInfo.isLegalIdentity(el))
                .collect(Collectors.toList()).isEmpty();
    }

    private boolean isMe(NodeInfo nodeInfo) {
        return nodeInfo.getLegalIdentities().get(0).getName().equals(me);
    }

    private boolean isNetworkMap(NodeInfo nodeInfo) {
        return nodeInfo.getLegalIdentities().get(0).getName().getOrganisation().equals("Network Map Service");
    }

    @Configuration
    class Plugin {
        @Bean
        public ObjectMapper registerModule() {
            return JacksonSupport.createNonRpcMapper();
        }
    }

    @GetMapping(value = "/status", produces = TEXT_PLAIN_VALUE)
    private String status() {
        return "200";
    }

    @GetMapping(value = "/addresses", produces = TEXT_PLAIN_VALUE)
    private String addresses() {
        return proxy.nodeInfo().getAddresses().toString();
    }

    @GetMapping(value = "/identities", produces = TEXT_PLAIN_VALUE)
    private String identities() {
        return proxy.nodeInfo().getLegalIdentities().toString();
    }

    @GetMapping(value = "/notaries", produces = TEXT_PLAIN_VALUE)
    private String notaries() {
        return proxy.notaryIdentities().toString();
    }

    @GetMapping(value = "/flows", produces = TEXT_PLAIN_VALUE)
    private String flows() {
        return proxy.registeredFlows().toString();
    }

    @GetMapping(value = "/states", produces = TEXT_PLAIN_VALUE)
    private String states() {
        return proxy.vaultQuery(ProductState.class).getStates().toString();
    }

    @GetMapping(value = "/peers", produces = APPLICATION_JSON_VALUE)
    public HashMap<String, List<String>> getPeers() {
        HashMap<String, List<String>> myMap = new HashMap<>();

        // Find all nodes that are not notaries, ourself, or the network map.
        Stream<NodeInfo> filteredNodes = proxy.networkMapSnapshot().stream()
                .filter(el -> !isNotary(el) && !isMe(el) && !isNetworkMap(el));
        // Get their names as strings
        List<String> nodeNames = filteredNodes.map(el -> el.getLegalIdentities().get(0).getName().toString())
                .collect(Collectors.toList());

        myMap.put("peers", nodeNames);
        return myMap;
    }

    @GetMapping(value = "/me", produces = APPLICATION_JSON_VALUE)
    private HashMap<String, String> whoami() {
        HashMap<String, String> myMap = new HashMap<>();
        myMap.put("me", me.toString());
        return myMap;
    }

    @GetMapping(value = "/currentuser", produces = APPLICATION_JSON_VALUE)
    private HashMap<String, String> currentuser() {
        HashMap<String, String> myMap = new HashMap<>();
        myMap.put("host", host);
        myMap.put("username", username);
        myMap.put("pass", password);
        myMap.put("port", rpcPort);
        myMap.put("serverport", serverPort);

        for(Map.Entry m : myMap.entrySet()){    
            System.out.println(m.getKey()+" "+m.getValue());    
           }  
        
        return myMap;
    }

    @GetMapping(value = "/products", produces = APPLICATION_JSON_VALUE)
    public List<StateAndRef<ProductState>> getProducts() {
        return proxy.vaultQuery(ProductState.class).getStates();
    }

    @PostMapping(value = "create-product", produces = TEXT_PLAIN_VALUE, headers = "Content-Type=application/x-www-form-urlencoded")
    public ResponseEntity<String> product(HttpServletRequest request) throws IllegalArgumentException {
        int product_id = Integer.valueOf(request.getParameter("ProductID"));
        String name = request.getParameter("Name");
        String description = request.getParameter("Description");
        int quantity = Integer.valueOf(request.getParameter("Quantity"));

        try {

            SignedTransaction result = proxy
                    .startTrackedFlowDynamic(CreateProductFlow.class, product_id, name, description, quantity)
                    .getReturnValue().get();
            // Return the response.
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Transaction id " + result.getId() + " committed to ledger.\n "
                            + result.getTx().getOutput(0));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping(value = "update-product", produces = TEXT_PLAIN_VALUE, headers = "Content-Type=application/x-www-form-urlencoded")
    public ResponseEntity<String> updateproduct(HttpServletRequest request) throws IllegalArgumentException {
        int product_id = Integer.valueOf(request.getParameter("ProductID"));
        String name = request.getParameter("Name");
        String description = request.getParameter("Description");
        int quantity = Integer.valueOf(request.getParameter("Quantity"));

        try {

            SignedTransaction result = proxy
                    .startTrackedFlowDynamic(UpdateProductFlow.class, product_id, name, description, quantity)
                    .getReturnValue().get();
            // Return the response.
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Transaction id " + result.getId() + " committed to ledger.\n "
                            + result.getTx().getOutput(0));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping(value = "transfer-product", produces = TEXT_PLAIN_VALUE)
    public ResponseEntity<String> transferproduct(@RequestParam(value = "Productid") Integer Productid,
            @RequestParam(value = "newowner") String newowner) throws IllegalArgumentException {
        // UniqueIdentifier Prod_Id = new
        // UniqueIdentifier(null,UUID.fromString(Productid));
        Party new_owner = proxy.wellKnownPartyFromX500Name(CordaX500Name.parse(newowner));
        // System.out.println(Prod_Id);
        System.out.println(new_owner);
        try {

            SignedTransaction result = proxy
                    .startTrackedFlowDynamic(TransferProductFlow.Initiator.class, Productid, new_owner)
                    .getReturnValue().get();
            // Return the response.
            System.out.println(result);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Transaction id " + result.getId() + " committed to ledger.\n "
                            + result.getTx().getOutput(0));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping(value = "my-products", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StateAndRef<ProductState>>> getMyProducts() {
        List<StateAndRef<ProductState>> myproducts = proxy.vaultQuery(ProductState.class).getStates().stream().filter(
                it -> it.getState().getData().getowner().equals(proxy.nodeInfo().getLegalIdentities().get(0)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(myproducts);
    }
}
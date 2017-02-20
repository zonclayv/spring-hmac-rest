package by.haidash.server.config.hmac;

public class AuthorizationHeader {

    private final String algorithm;
    private final String apiKey;
    private final String nonce;
    private final byte[] digest;

    public AuthorizationHeader(String algorithm, String apiKey, String nonce, byte[] digest) {
        this.algorithm = algorithm;
        this.apiKey = apiKey;
        this.nonce = nonce;
        this.digest = digest;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getApiKey() {
        return apiKey;
    }

    public byte[] getDigest() {
        return digest;
    }

    public String getNonce() {
        return nonce;
    }
}

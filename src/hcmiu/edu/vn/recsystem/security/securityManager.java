package hcmiu.edu.vn.recsystem.security;

import java.util.UUID;

public class securityManager {
	public String generateToken() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replaceAll("-", "");
	}
}

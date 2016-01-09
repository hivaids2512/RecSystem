package hcmiu.edu.vn.recsystem.server;

import java.util.UUID;

public class test {
	public static void main(String args[]) {
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid.replaceAll("-", ""));
	}
}

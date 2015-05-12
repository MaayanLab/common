package edu.mssm.pharm.maayanlab.common.core;

import java.io.IOException;
import java.net.URISyntaxException;

import edu.mssm.pharm.maayanlab.common.core.hdfs.HDFSClient;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class HdfsTest extends TestCase{

	public static Test suite() {
		return new TestSuite( HdfsTest.class );
	}
	
	public void test() throws IOException, URISyntaxException {
		HDFSClient client = new HDFSClient("hdfs://elizabeth:8020");
		client.makeDirectory("/tmp/testDir");
		client.remove("/tmp/testDir");
	}
}

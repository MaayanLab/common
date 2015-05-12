package edu.mssm.pharm.maayanlab.common.core.hdfs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DFSClient;

/**
 * HDFS client
 * 
 * @author Michael McDermott
 * @since April 09, 2015
 */

public class HDFSClient {

	DFSClient client;
	
	public HDFSClient(String hdfsUrl) throws IOException, URISyntaxException {
		Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        conf.set("dfs.permissions", "false");
        client = new DFSClient(new URI(hdfsUrl), conf);
	}
	
	public void uploadFile(InputStream stream, String destinationFilename) throws IOException{
  
        OutputStream out = null;
        InputStream in = null;
        try {
            if (client.exists(destinationFilename)) {
                System.out.println("File already exists in hdfs: " + destinationFilename);
                return;
            }
            out = new BufferedOutputStream(client.create(destinationFilename, false));
            in = new BufferedInputStream(stream);
            byte[] buffer = new byte[1024];

            int len;
            while ((len = in.read(buffer)) > 0)
                out.write(buffer, 0, len);
        } finally {
            client.close();
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }
	
	public void makeDirectory(String path) throws IOException{
		client.mkdirs(path, new FsPermission(FsAction.ALL, FsAction.ALL, FsAction.READ_EXECUTE), false);
	}
	
	public void remove(String path) throws IOException{
		client.delete(path, true);
	}

}

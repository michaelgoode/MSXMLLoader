package FileHandler;

/* <!-- in case someone opens this in a browser... --> <pre> */
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/** This is a simple wrapper around the Jakarta Commons FTP
  * library. I really just added a few convenience methods to the
  * class to suit my needs and make my code easier to read.
  * <p>
  * If you want more information on the Jakarta Commons libraries
  * (there is a LOT more you can do than what you see here), go to:
  *		http://jakarta.apache.org/commons
  * <p>
  * This Java class requires both the Jakarta Commons Net library
  * and the Jakarta ORO library (available at http://jakarta.apache.org/oro ).
  * Make sure you have both of the jar files in your path to compile.
  * Both are free to use, and both are covered under the Apache license
  * that you can read on the apache.org website. If you plan to use these
  * libraries in your applications, please refer to the Apache license first.
  * While the libraries are free, you should double-check to make sure you
  * don't violate the license by using or distributing it (especially if you use it
  * in a commercial application).
  * <p>
  * Program version 1.0. Author Julian Robichaux, http://www.nsftools.com
  *
  * @author Julian Robichaux ( http://www.nsftools.com )
  * @version 1.0
  */



public class JakartaFtpWrapper extends FTPClient  {

	/** A convenience method for connecting and logging in */
	public boolean connectAndLogin (String host, String userName, String password)
			throws  IOException, UnknownHostException, FTPConnectionClosedException {
		boolean success = false;
		connect(host);
		int reply = getReplyCode();
		if (FTPReply.isPositiveCompletion(reply))
			success = login(userName, password);
                else throw new IOException(String.format("Host %s failed to reply.....",host));

		if (!success)
			disconnect();
		return success;
	}

        public boolean simplyConnect (String host)
			throws  IOException, UnknownHostException, FTPConnectionClosedException {
		boolean success = false;
		connect(host);
		int reply = getReplyCode();
		if (FTPReply.isPositiveCompletion(reply))
			success = true;
		if (!success)
			disconnect();
		return success;
	}


	/** Turn passive transfer mode on or off. If Passive mode is active, a
	  * PASV command to be issued and interpreted before data transfers;
	  * otherwise, a PORT command will be used for data transfers. If you're
	  * unsure which one to use, you probably want Passive mode to be on. */
	public void setPassiveMode(boolean setPassive) {
		if (setPassive)
			enterLocalPassiveMode();
		else
			enterLocalActiveMode();
	}

	/** Use ASCII mode for file transfers */
	public boolean ascii () throws IOException {
		return setFileType(FTP.ASCII_FILE_TYPE);
	}

	/** Use Binary mode for file transfers */
	public boolean binary () throws IOException {
		return setFileType(FTP.BINARY_FILE_TYPE);
	}

	/** Download a file from the server, and save it to the specified local file */
	public boolean downloadFile (String serverFile, String localFile)
			throws IOException, FTPConnectionClosedException {
		ByteArrayOutputStream byteArrayOut =new ByteArrayOutputStream();
		boolean result = retrieveFile(serverFile, byteArrayOut);
		if (result) {
			FileOutputStream out = new FileOutputStream(localFile);
			out.write(byteArrayOut.toByteArray());
			out.close();
		}
		byteArrayOut.close();
		return result;
	}

	/** Upload a file to the server */
	public boolean uploadFile (String localFile, String serverFile)
			throws IOException, FTPConnectionClosedException {
		FileInputStream in = new FileInputStream(localFile);
                setFileType(FTPClient.BINARY_FILE_TYPE);
		boolean result = storeFile(serverFile, in);
		in.close();
		return result;
	}

	/** Get the list of files in the current directory as a Vector of Strings
	  * (excludes subdirectories) */
	public Vector listFileNames ()
			throws IOException, FTPConnectionClosedException {
		FTPFile[] files = listFiles();
		Vector v = new Vector();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].isDirectory())
				v.addElement(files[i].getName());
		}
		return v;
	}

	/** Get the list of files in the current directory as a single Strings,
	  * delimited by \n (char '10') (excludes subdirectories) */
	public String listFileNamesString ()
			throws IOException, FTPConnectionClosedException {
		return vectorToString(listFileNames(), "\n");
	}

	/** Get the list of subdirectories in the current directory as a Vector of Strings
	  * (excludes files) */
	public Vector listSubdirNames ()
			throws IOException, FTPConnectionClosedException {
		FTPFile[] files = listFiles();
		Vector v = new Vector();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				v.addElement(files[i].getName());
		}
		return v;
	}

	/** Get the list of subdirectories in the current directory as a single Strings,
	  * delimited by \n (char '10') (excludes files) */
	public String listSubdirNamesString ()
			throws IOException, FTPConnectionClosedException {
		return vectorToString(listSubdirNames(), "\n");
	}

	/** Convert a Vector to a delimited String */
	private String vectorToString (Vector v, String delim) {
		StringBuffer sb = new StringBuffer();
		String s = "";
		for (int i = 0; i < v.size(); i++) {
			sb.append(s).append((String)v.elementAt(i));
			s = delim;
		}
		return sb.toString();
	}

}


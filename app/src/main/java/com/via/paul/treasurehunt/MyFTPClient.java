package com.via.paul.treasurehunt;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.*;
import org.apache.commons.net.telnet.TelnetOption;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class MyFTPClient {

    public FTPClient mFTPClient = null;

    public void ftpConnect(String host, String username,
                           String password, int port) {
        try {
            mFTPClient = new FTPClient();
            mFTPClient.connect(host, port);
            mFTPClient.login(username, password);
            mFTPClient.enterLocalPassiveMode();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
        } catch (Exception e) {
        }
    }

    public FTPFile[] getFiles() {
        mFTPClient.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
        try {
            return mFTPClient.listFiles();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String ftpGetCurrentWorkingDirectory() {
        try {
            String workingDir = mFTPClient.printWorkingDirectory();
            return workingDir;
        } catch (Exception e) {
        }

        return null;
    }


    public boolean ftpChangeDirectory(String directory_path) {
        try {
            mFTPClient.changeWorkingDirectory(directory_path);
        } catch (Exception e) {
        }

        return false;
    }


    public boolean ftpDownload(String srcFilePath, String desFilePath) {
        boolean status = false;
        try {
            FileOutputStream desFileStream = new FileOutputStream(desFilePath);
            status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
            desFileStream.close();

            return status;
        } catch (Exception e) {
        }

        return status;
    }


    public boolean ftpUpload(String srcFilePath, String desFileName,
                             String desDirectory, Context context) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(new File(srcFilePath));

            status = mFTPClient.storeFile(desFileName, srcFileStream);

            srcFileStream.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
}
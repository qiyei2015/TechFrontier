package com.androidlib.net.entity;

import android.text.TextUtils;

import com.androidlib.utils.BaseUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by daner on 2016/5/7.
 * 1273482124@qq.com
 * POST报文格式请参考博客 : http://blog.csdn.net/bboyfeiyu/article/details/41863951.
 * Android中的多参数类型的Entity实体类,用户可以使用该类来上传文件、文本参数、二进制参数,
 * 不需要依赖于httpmime.jar来实现上传文件的功能.
 */
public class MultipartEntity implements HttpEntity {

    private static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 换行符
     */
    private final String NEW_LINE = "\r\n";
    private final String CONTENT_TYPE = "Content-Type: ";
    private final String CONTENT_DISPOSITION = "Content-Disposition: ";
    /**
     * 文本参数与字符编码集
     */
    private final String TYPE_TEXT_CHARSET = "text/plain; charset=UTF-8";
    /**
     * 字节流参数
     */
    private final String TYPE_OCTET_STREAM = "application/octet-stream";
    /**
     * 二进制参数
     */
    private final byte[] BINARY_ENCODING = "Content-Transfer-Encoding: binary\r\n\r\n".getBytes();
    /**
     * 文本参数
     */
    private final byte[] BIT_ENCODING = "Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes();
    /**
     * 分隔符
     */
    private String mBoundary = null;
    /**
     * 输出流
     */
    ByteArrayOutputStream mOutputStream = new ByteArrayOutputStream();

    public MultipartEntity(){
        this.mBoundary = generateBoundary();
    }

    /**
     * 生成分隔符
     * @return 返回生成的分隔符
     */
    private final String generateBoundary(){
        final StringBuffer buffer = new StringBuffer();
        final Random random = new Random();
        for (int i = 0;i < 30;i++) {
            buffer.append(MULTIPART_CHARS[random.nextInt(MULTIPART_CHARS.length)]);
        }
        return buffer.toString();
    }

    /**
     * 参数开头的分隔符
     */
    private void writeFirstBoundary(){
        try {
            mOutputStream.write(("--" + mBoundary + "\r\n\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加文本参数
     * @param paramName
     * @param value
     */
    public void addStringPart(final String paramName,final String value){

    }

    /**
     * 将数据写入到输出流中
     * @param paramName 参数名字
     * @param rawData 原始数据
     * @param type 参数类型
     * @param encodingBytes 编码方式
     * @param file 文件名称
     */
    private void writeToOutputStream(String paramName,byte[] rawData,String type
            ,byte[] encodingBytes,String file){

        try {
            writeFirstBoundary();
            mOutputStream.write((CONTENT_TYPE + type + NEW_LINE).getBytes());
            mOutputStream.write(getContentDispositionBytes(paramName,file));
            mOutputStream.write(encodingBytes);
            mOutputStream.write(rawData);
            mOutputStream.write(NEW_LINE.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * 获取内容描述
     * @param paramName
     * @param fileName
     * @return
     */
    private byte[] getContentDispositionBytes(String paramName,String fileName){
        StringBuffer buffer = new StringBuffer();
        buffer.append(CONTENT_DISPOSITION + "from-data; name=\"" + paramName + "\"");
        //文本参数没有fileName.设置为空即可
        if (!TextUtils.isEmpty(fileName)){
            buffer.append("; filename=\"" + fileName + "\"");
        }
        return buffer.append(NEW_LINE).toString().getBytes();
    }

    /**
     * 添加二进制参数, 例如Bitmap的字节流参数
     * @param paramName
     * @param rawData
     */
    public void addBinaryPart(String paramName,final byte[] rawData){
        writeToOutputStream(paramName,rawData,TYPE_OCTET_STREAM,BINARY_ENCODING,"no-file");
    }

    /**
     * 添加文件参数,可以实现文件上传功能
     * @param key
     * @param file
     */
    public void addFilePart(final String key, final File file){
        InputStream fin = null;
        try {
            fin = new FileInputStream(file);
            writeFirstBoundary();
            String type = CONTENT_TYPE + TYPE_OCTET_STREAM + NEW_LINE;
            mOutputStream.write(getContentDispositionBytes(key,file.getName()));
            mOutputStream.write(type.getBytes());
            mOutputStream.write(BINARY_ENCODING);
            final byte[] tmp = new byte[4096];
            int len = 0;
            while ((len = fin.read(tmp)) != -1){
                mOutputStream.write(tmp,0,len);
            }
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            BaseUtils.close(fin);
        }

    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        return new ByteArrayInputStream(mOutputStream.toByteArray());
    }

    @Override
    public void consumeContent() throws IOException {
        if (isStreaming()){
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        //参数最末尾的结束符
        String endString = "--" + mBoundary + "--\r\n";
        //写入结束符
        mOutputStream.write(endString.getBytes());
        outputStream.write(mOutputStream.toByteArray());
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public long getContentLength() {
        return mOutputStream.toByteArray().length;
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public Header getContentType() {
        return null;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }
}

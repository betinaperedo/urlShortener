package coop.tecso.main.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.mockito.internal.invocation.SerializableMethod;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;

public class ShortUrl implements Serializable{
	private String shortUrl;
	private String longtUrl;
	
	public ShortUrl(){
		
		
	}
	public ShortUrl(String shortUrl, String longtUrl) {
		this.shortUrl=shortUrl;
		this.longtUrl=longtUrl;
	}
	public String getShortUrl() {
		return shortUrl;
	}
	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}
	public String getLongtUrl() {
		return longtUrl;
	}
	public void setLongtUrl(String longtUrl) {
		this.longtUrl = longtUrl;
	}
	
	public static String getShortUrlSha1Implementation(String url) {				
		return toSHA1(url);
	}
	private static String toSHA1(String s) {
		HashFunction hf = Hashing.sha1();		
		HashCode hashCode=hf.hashString(s, Charset.defaultCharset());
		byte[] sha1 = hashCode.asBytes();
	    String shaStr =BaseEncoding.base64Url().encode(sha1);;
	    shaStr = shaStr.substring(0, 6);	    
	    try {
			return URLEncoder.encode(shaStr, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw Throwables.propagate(e);
		}
	}
	@Override
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof ShortUrl){			
			ShortUrl shortUrlComper = (ShortUrl) obj;
			return shortUrl.equals(shortUrlComper.getShortUrl()) && longtUrl.equals(shortUrlComper.getLongtUrl());
		}
		else
			return false;
		
		
	}

}

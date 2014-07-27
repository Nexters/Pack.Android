package com.nexters.pack.util;

public class MainItem {

	private String text;
	private int image;

	public MainItem(String text, int iamge) {
		this.text = text;
		this.image = image;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	
	
}

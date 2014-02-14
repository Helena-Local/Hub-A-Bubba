/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base;

/**
 * Created by abbie on 2/6/14.
 */
public abstract class HubInit {
    public enum HubType {BUYER_HUB, CERTIFICATION_HUB, INIT_HUB, ITEM_HUB, ORDER_HUB, PRODUCER_HUB, AD_HUB}

    // defaults set below
    protected static String logTag = "Hub ";

    protected static String hubName = "Hub Name Uninitialized...";
    protected static String dataVersion = "v0.0";
    protected static String dataVersionNotes = "Uninitialized...";

    protected static String initHubDataUrl = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=5&output=csv";

    protected static String growerHubDataUrl = "";
    protected static String producerHubDataUrl = "";
    protected static String orderHubDataUrl = "";
    protected static String itemHubDataUrl = "";
    protected static String buyerHubDataUrl = "";
    protected static String certificationHubDataUrl = "";
    protected static String adHubDataUrl = "";

    protected static String hubEmailTo = "info@helenalocal.org";
    protected static String hubEmailSubject = "HL Hub - Request email...";

    protected static long buyerDelay = 5;
    protected static long itemDelay = 1;
    protected static long orderDelay = 1;
    protected static long producerDelay = 5;
    protected static long certificateDelay = 5;
    protected static long adDelay = 1;
    protected static long adPlayRate = 1;

    public static long getBuyerDelay() {
        return buyerDelay;
    }

    public static long getItemDelay() {
        return itemDelay;
    }

    public static long getOrderDelay() {
        return orderDelay;
    }

    public static long getProducerDelay() {
        return producerDelay;
    }

    public static String getHubEmailTo() {
        return hubEmailTo;
    }

    public static String getHubEmailSubject() {
        return hubEmailSubject;
    }

    public static String getHubName() {
        return hubName;
    }

    public static String getDataVersion() {
        return dataVersion;
    }

    public static String getDataVersionNotes() {
        return dataVersionNotes;
    }

    public static String getInitHubDataUrl() {
        return initHubDataUrl;
    }

    public static String getGrowerHubDataUrl() {
        return growerHubDataUrl;
    }

    public static String getProducerHubDataUrl() {
        return producerHubDataUrl;
    }

    public static String getOrderHubDataUrl() {
        return orderHubDataUrl;
    }

    public static String getItemHubDataUrl() {
        return itemHubDataUrl;
    }

    public static String getBuyerHubDataUrl() {
        return buyerHubDataUrl;
    }

    public static String getCertificationHubDataUrl() {
        return certificationHubDataUrl;
    }

    public static String getAdHubDataUrl() {
        return adHubDataUrl;
    }

    public static long getCertificateDelay() {
        return certificateDelay;
    }

    public static long getAdDelay() {
        return adDelay;
    }

    public static long getAdPlayRate() {
        return adPlayRate;
    }

    public static void setAdPlayRate(long adPlayRate) {
        HubInit.adPlayRate = adPlayRate;
    }

    public static void setAdDelay(long adDelay) {
        HubInit.adDelay = adDelay;
    }

    public static void setCertificateDelay(long certificateDelay) {
        HubInit.certificateDelay = certificateDelay;
    }

    public static void setCertificationHubDataUrl(String certificationHubDataUrl) {
        HubInit.certificationHubDataUrl = certificationHubDataUrl;
    }

    public static void setAdHubDataUrl(String adHubDataUrl) {
        HubInit.adHubDataUrl = adHubDataUrl;
    }

    public static void setDataVersionNotes(String dataVersionNotes) {
        HubInit.dataVersionNotes = dataVersionNotes;
    }

    public static void setDataVersion(String dataVersion) {
        HubInit.dataVersion = dataVersion;
    }

    public static void setHubName(String hubName) {
        HubInit.hubName = hubName;
    }

    public static void setHubEmailTo(String hubEmailTo) {
        HubInit.hubEmailTo = hubEmailTo;
    }

    public static void setHubEmailSubject(String hubEmailSubject) {
        HubInit.hubEmailSubject = hubEmailSubject;
    }

    public static void setGrowerHubDataUrl(String growerHubDataUrl) {
        HubInit.growerHubDataUrl = growerHubDataUrl;
    }

    public static void setProducerHubDataUrl(String producerHubDataUrl) {
        HubInit.producerHubDataUrl = producerHubDataUrl;
    }

    public static void setOrderHubDataUrl(String orderHubDataUrl) {
        HubInit.orderHubDataUrl = orderHubDataUrl;
    }

    public static void setItemHubDataUrl(String itemHubDataUrl) {
        HubInit.itemHubDataUrl = itemHubDataUrl;
    }

    public static void setBuyerHubDataUrl(String buyerHubDataUrl) {
        HubInit.buyerHubDataUrl = buyerHubDataUrl;
    }

    public static void setItemDelay(long itemDelay) {
        HubInit.itemDelay = itemDelay;
    }

    public static void setBuyerDelay(long buyerDelay) {
        HubInit.buyerDelay = buyerDelay;
    }

    public static void setOrderDelay(long orderDelay) {
        HubInit.orderDelay = orderDelay;
    }

    public static void setProducerDelay(long producerDelay) {
        HubInit.producerDelay = producerDelay;
    }

    public static void setInitHubDataUrl(String initHubDataUrl) {
        HubInit.initHubDataUrl = initHubDataUrl;
    }

}

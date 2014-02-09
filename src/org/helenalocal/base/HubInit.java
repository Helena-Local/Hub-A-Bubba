/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base;

/**
 * Created by abbie on 2/6/14.
 */
public abstract class HubInit {
    public enum HubType {BUYER_HUB, CERTIFICATION_HUB, INIT_HUB, ITEM_HUB, ORDER_HUB, PRODUCER_HUB, GROWER_HUB}

    // defaults set below
    protected static String logTag = "Hub ";

    protected static String hubName = "Hub Name Uninitialized...";
    protected static String dataVersion = "v0.0";
    protected static String dataVersionNotes = "Uninitialized...";

    protected static String initHubDataUrl = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=5&output=csv";
    protected static String growerHubDataUrl = "https://docs.google.com/forms/d/14aZGVPlxgr6-9wH6OLyZfuSH-mF6vVJjzAbFxyRaqRc/formResponse";
    protected static String producerHubDataUrl = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=3&output=csv";
    protected static String orderHubDataUrl = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=2&output=csv";
    protected static String itemHubDataUrl = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=1&output=csv";
    protected static String buyerHubDataUrl = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=4&output=csv";
    protected static String certificationHubDataUrl = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=6&output=csv";

    protected static String hubEmailTo = "info@helenalocal.org";
    protected static String hubEmailSubject = "HL Hub - Request email...";

    protected static long buyerDelay = 5;
    protected static long itemDelay = 1;
    protected static long orderDelay = 1;
    protected static long producerDelay = 5;
    protected static long certificateDelay = 5;

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

    public static long getCertificateDelay() {
        return certificateDelay;
    }

    public static void setCertificateDelay(long certificateDelay) {
        HubInit.certificateDelay = certificateDelay;
    }

    public static void setCertificationHubDataUrl(String certificationHubDataUrl) {
        HubInit.certificationHubDataUrl = certificationHubDataUrl;
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

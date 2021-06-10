package com.example.monitoring.model;

import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp;
import java.util.Objects;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.io.IOUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;

@Data
@NoArgsConstructor
public class ResourceForMonitoring {
    @JsonProperty("Url")
    @JSONField(name="Url")
    String Url;
    CloseableHttpResponse lastUrlResponse;
    @JsonProperty("isActive")
    @JSONField(name="isActive")
    boolean isActive = true;
    Timestamp requestResendTime=new Timestamp(new Date().getTime());
    @JsonProperty("requestResendDelay")
    @JSONField(name="requestResendDelay")
    int requestResendDelay;
    int id;
    @JsonProperty("userId")
    @JSONField(name="userId")
    int userId;
    @JsonProperty("minResponseTimeForOk")
    @JSONField(name="minResponseTimeForOk")
    int minResponseTimeForOk;
    @JsonProperty("maxResponseTimeForOk")
    @JSONField(name="maxResponseTimeForOk")
    int maxResponseTimeForOk;
    @JsonProperty("maxResponseTimeForWarning")
    @JSONField(name="maxResponseTimeForWarning")
    int maxResponseTimeForWarning;
    @JsonProperty("minResponseSize")
    @JSONField(name="minResponseSize")
    int minResponseSize;
    @JsonProperty("maxResponseSize")
    @JSONField(name="maxResponseSize")
    int maxResponseSize;
    String urlStatus;
    Timestamp lastRequestSendTime;
    @JsonProperty("expectedResponseCode")
    @JSONField(name="expectedResponseCode")
    int expectedResponseCode;
    long responseSize;



    public void setLastUrlResponse(CloseableHttpResponse lastUrlResponse) throws IOException {
        this.lastUrlResponse = lastUrlResponse;
        byte[] data = IOUtils.toByteArray(this.lastUrlResponse.getEntity().getContent());
        responseSize = data.length;

        if (Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) > minResponseTimeForOk
                && responseSize > minResponseSize
                && responseSize < maxResponseSize
                && Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) < maxResponseTimeForOk
                && lastUrlResponse.getStatusLine().getStatusCode() == expectedResponseCode) {
            urlStatus = "OK";
        } else if (Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) > maxResponseTimeForOk
                && responseSize > minResponseSize && responseSize < maxResponseSize
                && Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) < maxResponseTimeForWarning
                && lastUrlResponse.getStatusLine().getStatusCode() == expectedResponseCode) {
            urlStatus = "WARNING";
        } else if (Long.parseLong(lastUrlResponse.getFirstHeader("responseTime").getValue()) > maxResponseTimeForWarning
                || responseSize < minResponseSize || responseSize > maxResponseSize
                || lastUrlResponse.getStatusLine().getStatusCode() != expectedResponseCode ) {
            urlStatus = "CRITICAL";
        }
    }
    public void setRequestResendTime() {
        this.requestResendTime = new Timestamp(new Date().getTime()+requestResendDelay * 1000);
    }

    public String getUrlStatusForView() {
        if ("CRITICAL".equals(urlStatus)) {
            return "<font color=\"red\">" + urlStatus + "</font>" + "<audio autoplay>\n"
                    + "    <source src=\"/mavenproject1/resource/CRITICAL.mp3\" type=\"audio/mpeg\">"
                    + "  </audio>";
        }
        if ("WARNING".equals(urlStatus)) {
            return "<font color=\"yellow\">" + urlStatus + "</font>" + "<audio autoplay>\n"
                    + "    <source src=\"/mavenproject1/resource/WARNING.mp3\" type=\"audio/mpeg\">"
                    + "  </audio>";
        }
        return urlStatus;
    }

    public String getResponseCode(){
        return lastUrlResponse.getStatusLine().toString();
    }

    public String getResponseTime(){
        return getLastUrlResponse().getFirstHeader("responseTime").getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceForMonitoring resource = (ResourceForMonitoring) o;
        return id == resource.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

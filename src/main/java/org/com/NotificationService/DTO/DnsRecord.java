package org.com.NotificationService.DTO;


public record DnsRecord(
        String type,     // "CNAME"
        String name,     // "vvjuipp...._domainkey.acme.com"
        String value     // "vvjuipp....dkim.amazonses.com"
) {}

package com.example.document.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentMetadataRequest {
    private String status; // e.g., "VERIFIED" or "PENDING"
    private String title;  // new document name (optional)
}

package com.example.document.controller;

import com.example.document.dto.DocumentRequest;
import com.example.document.dto.DocumentMetadataRequest;
import com.example.document.model.Document;
import com.example.document.service.DocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // ✅ Create a document
    @PostMapping
    public Document create(@RequestBody DocumentRequest request) {
        return documentService.createDocument(request);
    }

    // ✅ Get documents related to the user's departments
    @GetMapping
    public List<Document> getMyDocuments() {
        return documentService.getMyDocuments();
    }

    // ✅ Edit metadata (only creator allowed)
    @PutMapping("/{id}/metadata")
    public Document updateMetadata(@PathVariable Long id, @RequestBody DocumentMetadataRequest request) {
        return documentService.updateMetadata(id, request);
    }

    // ✅ Delete a document (only creator allowed)
    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
    }
}

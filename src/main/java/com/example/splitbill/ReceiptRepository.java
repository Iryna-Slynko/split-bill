package com.example.splitbill;

import com.example.splitbill.models.Receipt;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

import reactor.core.publisher.Flux;

public interface ReceiptRepository  extends FirestoreReactiveRepository<Receipt> {
  Flux<Receipt> findBy(Pageable pageable);
}

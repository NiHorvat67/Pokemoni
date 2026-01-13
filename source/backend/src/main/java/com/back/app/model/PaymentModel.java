package com.back.app.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;





@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "payment")
public class PaymentModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer payment_id;


    @Column(name = "payer_id", unique = true)
    private Integer payer_id;

    @Column(name = "payment_description")
    private String payment_description;

    @Column(name = "payment_date")
    private LocalDate payment_date;


}

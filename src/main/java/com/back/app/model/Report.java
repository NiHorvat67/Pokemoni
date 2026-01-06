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

/*


 report_id | reporter_id | reported_id |                  
              report_details                           
                   | report_status |        
                     created_at


*/

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer report_id;

    @Column(name = "reporter_id")
    private Integer reporter_id;

    @Column(name = "reported_id")
    private Integer reported_id;

    @Column(name = "report_details")
    private String report_details;

    @Column(name = "report_status")
    private String report_status;
    
}

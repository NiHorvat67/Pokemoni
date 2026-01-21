package com.back.app.model;

import java.time.LocalDateTime;

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

  @Column(name = "created_at")
  private LocalDateTime created_at;

  public Report(Integer reporter_id, Integer reported_id, String report_details,
      String report_status, LocalDateTime created_at) {
    this.reporter_id = reporter_id;
    this.reported_id = reported_id;
    this.report_details = report_details;
    this.report_status = report_status;
    this.created_at = created_at;
  }

}

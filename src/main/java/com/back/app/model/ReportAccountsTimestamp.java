package com.back.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportAccountsTimestamp {
 
    
    private Report report;
    private Account reporter;
    private Account reported;


}

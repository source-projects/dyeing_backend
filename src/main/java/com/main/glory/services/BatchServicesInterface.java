package com.main.glory.services;

import com.main.glory.model.BatchGrDetail;
import com.main.glory.model.BatchMast;

import java.util.List;

public interface BatchServicesInterface {
    public int saveBatch(BatchMast batchMast) throws Exception;
    public List<BatchMast> getAllBatch();
    public boolean deleteBatchById(Long id);
    public boolean updateBatchById(BatchMast bm);
    public List<BatchGrDetail> getGrById(Long id);
}

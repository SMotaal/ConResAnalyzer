function row = FundamentalRow( dataTable, row, column, rowRange)
  %FUNDAMENTALROW Summary of this function goes here
  %   Detailed explanation goes here
  
  
    minRow                    = 1;
    maxRow                    = size(dataTable, 1);

    if ~exist('rowRange', 'var') || ~isnumeric(rowRange)
      rowRange                = [-1:+1];
    end
    
    rowRange                  = round(row) + rowRange;
    
    rowRange(rowRange<minRow) = minRow;
    rowRange(rowRange>maxRow) = maxRow;
    
    [maxValue rowIdx]         = max(dataTable(rowRange, column));
    row                       = rowRange(rowIdx); %round(mfQ); % + fQRow;
  
end


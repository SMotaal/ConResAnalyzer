classdef Math
  %FOURIER Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
  end
  
  methods (Static)
    [fQ]        = FundamentalFrequency(lpmm, mm);
    [row]       = FundamentalRow( dataTable, row, column, rowRange);    
    [B W]       = FrequencyRange(diameter, ppi);
    [Lp Lm]     = VisualResolution(ppi, va, vd);
  end
  
end


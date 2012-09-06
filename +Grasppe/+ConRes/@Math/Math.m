classdef Math
  %FOURIER Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
  end
  
  methods (Static)
    F           = FundamentalFrequency(resolution, diameter, ppi);
    [B W]       = FrequencyRange(diameter, ppi);
    [Lp Lm]     = VisualResolution(ppi, va, vd);
  end
  
end


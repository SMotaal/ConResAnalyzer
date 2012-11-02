function F = FundamentalFrequency(lpmm, mm, ppi, ppm)
  if nargin<4, ppm  = ppi/25.4; end
  %diameter          = ceil(mm*ppm);
  F                 = (ppm/lpmm)/2; %* diameter * 2;
end

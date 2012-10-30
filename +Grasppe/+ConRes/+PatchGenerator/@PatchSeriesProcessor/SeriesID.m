function seriesID = SeriesID(seriesID)
  
  persistent SERIESID;
  
  if nargin>0 && ischar(seriesID)
    SERIESID  = seriesID;
  end
  
  if isempty(SERIESID)
    SERIESID  = 'Series104a';
  end
  
  seriesID      = SERIESID;
  
end


function seriesID = SeriesID(seriesID)
  
  persistent SERIESID;
  
  if isempty(SERIESID) 
    if nargin>0 && ischar(seriesID)
      SERIESID  = seriesID;
    else
      SERIESID  = 'Series104a';
    end
  end
  
  seriesID      = SERIESID;
  
end


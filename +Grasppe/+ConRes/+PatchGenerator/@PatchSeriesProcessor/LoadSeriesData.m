function data = LoadSeriesData(seriesID, varargin)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor;
  
  PatchSeriesProcessor.SeriesID(seriesID);
  
  data = PatchSeriesProcessor.LoadData(varargin{:});
  
end


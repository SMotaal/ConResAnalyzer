function data = LoadSeriesData(seriesID, varargin)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor;
  PatchSeriesProcessor.SeriesID('Series104d');
  
  data = PatchSeriesProcessor.LoadData(varargin{:});
  
end


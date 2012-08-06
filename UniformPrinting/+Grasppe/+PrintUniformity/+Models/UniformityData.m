classdef UniformityData < Grasppe.Data.Models.DataModel
  %UNIFORMITYDATA Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Parameters
    CaseData
    SetData
    SheetData
  end
  
  methods
    function obj = UniformityData(varargin)
      obj = obj@Grasppe.Data.Models.DataModel(varargin{:});
      
      if isempty(obj.Parameters) 
        obj.Parameters = Grasppe.PrintUniformity.Models.DataParameters('Creator', obj);
      end
        
    end   
  end
  
  methods (Access = protected)
    % Override copyElement method:
    function cpObj = copyElement(obj)
      % Make a shallow copy of all shallow properties
      cpObj = copyElement@Grasppe.Data.Models.DataModel(obj);
      
      % Make a deep copy of the deep object
      try cpObj.Parameters = copy(obj.Parameters); end
    end
  end
  
end


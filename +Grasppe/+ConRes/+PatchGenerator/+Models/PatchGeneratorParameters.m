classdef PatchGeneratorParameters < handle & matlab.mixin.Copyable
  %PATCHGENERATORPARAMETERS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    Patch   = Grasppe.ConRes.PatchGenerator.Models.PatchParameters;
    Screen  = Grasppe.ConRes.PatchGenerator.Models.ScreenParameters;
    % Print   = Grasppe.ConRes.PatchGenerator.Models.PrintParameters;
    Scan    = Grasppe.ConRes.PatchGenerator.Models.ScanParameters;
    Processors = [];
  end
  
   methods(Access = protected)
       
      function cpObj = copyElement(obj)
         % Make a shallow copy of all four properties
         cpObj = copyElement@matlab.mixin.Copyable(obj);
         
         % Make a deep copy of the DeepCp object
         fields = fieldnames(obj);
         for m = 1:numel(fields)
           field = fields{m};
           try cpObj.(field) = copy(obj.(field)); end
         end
         
      end
   end
  
end


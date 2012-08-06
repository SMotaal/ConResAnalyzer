classdef UniformityPlotFigure < Grasppe.Graphics.MultiPlotFigure
  %UNIFORMITYPLOTFIGURE Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    PlotMediator
    DataSources
  end
  
  methods
    function obj = UniformityPlotFigure(varargin)
      obj = obj@Grasppe.Graphics.MultiPlotFigure(varargin{:});
      refresh(obj.Handle);
    end   

    function prepareMediator(obj, dataProperties, axesProperties)
      if isempty(obj.PlotMediator) || ~isa(obj, 'Grasppe.PrintUniformity.UI.PlotMediator')
        obj.PlotMediator  = Grasppe.PrintUniformity.UI.PlotMediator;
      end
      
      obj.registerHandle(obj.PlotMediator);
      
      obj.attachMediations(obj.PlotAxes, axesProperties);
      
      obj.attachMediations(obj.DataSources, dataProperties);
      
      obj.PlotMediator.createControls(obj);
    end
    
  end
  
  methods(Access=protected)
    
    function createComponent(obj)
      obj.createComponent@Grasppe.Graphics.MultiPlotFigure();
    end
        
    function attachMediations(obj, subjects, properties)
      
      plotMediator  = obj.PlotMediator;
      
      subjects = subjects;
      
      for m = 1:numel(subjects)
        subject = subjects{m};
        for n = 1:numel(properties)
          property = properties{n};
          if isa(property, 'char')
            alias     = property;
          elseif isa(property, 'cell')
            alias     = property{1};
            property  = property{2};
          else
            continue;
          end
          plotMediator.attachMediatorProperty(subject, property, alias);
        end
      end
      
      obj.PlotMediator = plotMediator;
      
    end
    
    
%     function preparePlotAxes(obj)
%       obj.preparePlotAxes@Grasppe.Graphics.MultiPlotFigure;
%     end
  end
  
  %     methods (Static, Hidden)
  %     function OPTIONS  = DefaultOptions()
  %       PlotAxesLength = 1;
  %       Grasppe.Utilities.DeclareOptions;
  %     end
  %
  %     end
  
end


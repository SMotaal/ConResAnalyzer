function stats = GenerateSeriesStatistics(series, grids, fields, processors, parameters, task)
  %GENERATESERIESFFT Summary of this function goes here
  %   Detailed explanation goes here
  
%   import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
%   
%   forceGenerate             = false;
%   imageTypes                = {'halftone', 'screen', 'contone', 'monotone'};
%   halftoneOutput            = true;
%   retinaOutput              = true;
%   
%   try
%     OUTPUT                  = evalin('caller', 'output');
%     setOuput                = true;
%     
%     try if ~exist('series', 'var')  || isempty(series)
%         series              = OUTPUT.Series; end;
%     catch
%       series                = struct;
%     end
%     
%     try if ~exist('grids', 'var')   || isempty(grids)
%         grids               = OUTPUT.Grids; end;
%     end
%     
%     try if ~exist('fields', 'var')  || isempty(fields)
%         fields              = OUTPUT.Fields; end;
%     end
%     
%     try if ~exist('parameters', 'var')  || isempty(parameters)
%         parameters          = OUTPUT.Parameters; end;
%     end
%     
%     try if ~exist('processors', 'var')  || isempty(processors)
%         processors          = OUTPUT.Processors; end;
%     end
%     
%   catch err
%     setOuput                = false;
%   end
%   
%   try
%     if ~isfield(series, 'SRF')
%       SRF                     = PatchSeriesProcessor.LoadData('SRF', 'SRFOutput');
%       series.SRF              = SRF;
%     end
%   catch err
%     debugStamp(err, 1);
%   end
%   
%   setTask                   = exist('task', 'var') && isa(task, 'Grasppe.Occam.ProcessTask');
%   
%   fieldTable                = fields.Table;
%   seriesRows                = grids.Halftone.Rows;
%   seriesRange               = 1:seriesRows;
%   
%   halftoneRows              = grids.Halftone.Rows;
%   screenRows                = grids.Screen.Rows;
%   contoneRows               = grids.Contone.Rows;
%   monotoneRows              = grids.Monotone.Rows;
%   
%   screenIdxs                = grids.Screen.Index;
%   contoneIdxs               = grids.Contone.Index;
%   monotoneIdxs              = grids.Monotone.Index;
%   
%   screenRefs                = grids.Screen.Reference;
%   contoneRefs               = grids.Contone.Reference;
%   monotoneRefs              = grids.Monotone.Reference;
%   
%   seriesTable               = series.Table;
%   seriesParameters          = series.Parameters;
%   seriesVariables           = series.Variables;
%   
%   halftonePaths             = series.Paths.Halftone;
%   screenPaths               = series.Paths.Screen;
%   contonePaths              = series.Paths.Contone;
%   monotonePaths             = series.Paths.Monotone;
%   
%   seriesFFT                 = cell(seriesRows, 6);
%   halftoneFFTTable          = cell(seriesRows, 2);
%   screenFFTTable            = halftoneFFTTable; % cell(seriesRows, 2);
%   contoneFFTTable           = halftoneFFTTable; % cell(seriesRows, 2);
%   monotoneFFTTable          = halftoneFFTTable; % cell(seriesRows, 2);
%   
%   halftoneSRFTable          = halftoneFFTTable;
%   screenSRFTable            = halftoneSRFTable;
%   contoneSRFTable           = halftoneSRFTable;
%   monotoneSRFTable          = halftoneSRFTable;
%   
%   srfsAvailable             = isstruct(series) && isfield(series, 'SRF') && isstruct(series.SRF);
%   halftoneSRFAvailable      = srfsAvailable && isfield(series.SRF, 'Halftone') && iscell(series.SRF.Halftone);
%   screenSRFAvailable        = srfsAvailable && isfield(series.SRF, 'Screen')   && iscell(series.SRF.Screen);
%   contoneSRFAvailable       = srfsAvailable && isfield(series.SRF, 'Contone')  && iscell(series.SRF.Contone);
%   monotoneSRFAvailable      = srfsAvailable && isfield(series.SRF, 'Monotone') && iscell(series.SRF.Monotone);
%   
%   halftoneSRFs              = {};
%   screenSRFs                = {};
%   contoneSRFs               = {};
%   monotoneSRFs              = {};
%   
%   if halftoneSRFAvailable,  halftoneSRFs  = series.SRF.Halftone;  end
%   if screenSRFAvailable,    screenSRFs    = series.SRF.Screen;    end
%   if contoneSRFAvailable,   contoneSRFs   = series.SRF.Contone;   end
%   if monotoneSRFAvailable,  monotoneSRFs  = series.SRF.Monotone;  end
%   
%   %% Process FFT & SRF Data
%   try
%     parfor m = seriesRange
%       
%       %% Outputs
%       halftoneOutput          = true;
%       screenOutput            = any(m==screenIdxs);
%       contoneOutput           = any(m==contoneIdxs);
%       monotoneOutput          = any(m==monotoneIdxs);
%       
%       halftoneIdx             = m;
%       screenIdx               = screenRefs(m);
%       contoneIdx              = contoneRefs(m);
%       monotoneIdx             = monotoneRefs(m);
%       
%       halftoneID              = seriesTable{m, 4};
%       screenID                = seriesTable{m, 3};
%       contoneID               = seriesTable{m, 5};
%       monotoneID              = seriesTable{m, 6};
%       
%       bandParameters          = {[], []};
%       try
%         variables             = seriesVariables(m);
%         bandParameters{1}     = variables.Metrics.BandParameters(1);
%         bandParameters{2}     = variables.Metrics.BandParameters(2);
%       end
%       
%       %halftonePaths           = 
%       [FFT SRF FIMG IMG]      = ProcessImageFourier(imagePaths, bandParameters)
%       
%       
%     end
%   catch err
%     debugStamp(err,1);
%     beep;
%   end
%   
%   screenSRFTable                = screenSRFTable(screenIdxs, :);
%   contoneSRFTable               = contoneSRFTable(contoneIdxs, :);
%   monotoneSRFTable              = monotoneSRFTable(monotoneIdxs, :);
%   
%   output                    = struct;
%   output.SRF.Halftone       = halftoneSRFTable;
%   output.SRF.Screen         = screenSRFTable;
%   output.SRF.Contone        = contoneSRFTable;
%   output.SRF.Monotone       = monotoneSRFTable;
%   
%   
%   if ~isfield(series, 'SRF') || ~isequal(output.SRF, series.SRF)
%     PatchSeriesProcessor.SaveData(output, 'SRFOutput');
%     series.SRF              = output.SRF;
%   end
%   
%   if setOuput || nargout==0
%     OUTPUT.Series           = series;
%     assignin('caller', 'output', OUTPUT);
%   end
  
end

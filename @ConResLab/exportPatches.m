function [ output_args ] = exportPatches(data) % SRF, Series )
  %TALLYSRFDATA Summary of this function goes here
  %   Detailed explanation goes here
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  import Grasppe.ConRes.Math;
  
  global forceRenderPatches;
  
  forceRenderPatches        = isequal(forceRenderPatches, true); %false;  
%   forceOutput = true;
  
%   INT                       = '%d';
%   DEC                       = @(x) ['%1.' int2str(x) 'f'];
%   SCI                       = '%1.3fe%d';
%   STR                       = '%s';
%   TAB                       = '\t';
  
  if ~exist('data', 'var')
    data                    = PatchSeriesProcessor.LoadData();
    %     data.SRF                = PatchSeriesProcessor.LoadData('SRF', 'SRFData');
    %     data.PRF                = PatchSeriesProcessor.LoadData('PRF', 'PRFData');
  end
  
  parameters                = data.Series.Parameters;
  
  fieldTable                = data.Fields.Table;
  seriesRows                = data.Grids.Halftone.Rows;
  seriesRange               = 1:seriesRows;
  
  htRows                    = data.Grids.Halftone.Rows;
  scRows                    = data.Grids.Screen.Rows;
  ctRows                    = data.Grids.Contone.Rows;
  mtRows                    = data.Grids.Monotone.Rows;
  
  scIdxs                    = data.Grids.Screen.Index;
  ctIdxs                    = data.Grids.Contone.Index;
  mtIdxs                    = data.Grids.Monotone.Index;
  
  scRefs                    = data.Grids.Screen.Reference;
  ctRefs                    = data.Grids.Contone.Reference;
  mtRefs                    = data.Grids.Monotone.Reference;
  
  seriesTable               = data.Series.Table;
  seriesParameters          = data.Series.Parameters;
  seriesVariables           = data.Series.Variables;
  
  htPaths                   = data.Series.Paths.Halftone;
  scPaths                   = data.Series.Paths.Screen;
  ctPaths                   = data.Series.Paths.Contone;
  mtPaths                   = data.Series.Paths.Monotone;
  
%   htSRFs                    = data.SRF.Halftone;
%   scSRFs                    = data.SRF.Screen;
%   ctSRFs                    = data.SRF.Contone;
%   mtSRFs                    = data.SRF.Monotone;
%   
%   htPRFs                    = data.PRF.Monotone.Halftone;
%   scPRFs                    = data.PRF.Monotone.Screen;
%   ctPRFs                    = data.PRF.Monotone.Contone;
%   mtPRFs                    = data.PRF.Monotone.Monotone;
    
%   bandWidth   = 1;    % Data Columns = [B isum S imean istd mstd];
%   bandSum     = 2;
%   filterSum   = 3;
%   bandMean    = 4;
%   bandSigma   = 5;
  
  %% figure Columns
%   plotColumn                = bandMean;
%   labelColumns              = [bandMean, bandSigma];
%   
%   sumTable                  = zeros(seriesRows, 4 + 4 + 4 + 4); % + 4
%   outputText                = cell(seriesRows,1);
  
  %% Determine loop and display steps
  mStep                     = 1;  
  mRun                      = 0;  
  dStep                     = 50;
  dNext                     = dStep;
  
  for m = seriesRange(1:mStep:end)
    
    mRun   = mRun + mStep;
    
    try
    
    if mRun>=dNext
      dispf('Generating Series Patches... %d of %d', m, seriesRows);
      dNext = dNext + dStep;
    end
    
    %% Output Checks (All True!)
    
    %% Row Indices
    htIdx                   = m;
    scIdx                   = scRefs(m);
    ctIdx                   = ctRefs(m);
    mtIdx                   = mtRefs(m);
    
    %% Resource IDs
    htID                    = seriesTable{m, 4};
    scID                    = seriesTable{m, 3};
    ctID                    = seriesTable{m, 5};
    mtID                    = seriesTable{m, 6};
    
    %% Spatial Radial Frequency Data
    %     htSRF                   = htSRFs{htIdx, 2};
    %     scSRF                   = scSRFs{scIdx, 2};
    %     ctSRF                   = ctSRFs{ctIdx, 2};
    %     mtSRF                   = mtSRFs{mtIdx, 2};
    
    %% Cross-Product Spatial Radial Frequency Data
    %     htPRF                   = htPRFs{htIdx, 2};
    %     scPRF                   = scPRFs{scIdx, 2};
    %     ctPRF                   = ctPRFs{ctIdx, 2};
    %     mtPRF                   = mtPRFs{mtIdx, 2};
    
    %% Patch Parameters
    %     mRTV                    = parameters(m).Patch.Mean;
    %     mCON                    = parameters(m).Patch.Contrast;
    %     mRES                    = parameters(m).Patch.Resolution;
    %
    %     mSize                   = parameters(m).Patch.Size;
    %     mDPI                    = parameters(m).Scan.Resolution;
    %     mScale                  = parameters(m).Scan.Scale/100;
    %     mPPI                    = mDPI*mScale;
    
    %% Patch Variables
    %     mPixels                 = mDPI*mScale/25.4; %
    %     mfR                     = Math.VisualResolution(mPPI) * 7;
    %     mfQ                     = mRES/mSize * mPixels; %mRES/mPixels * mSize/mPixels * 2;
    %     [mBP mBW]               = Math.FrequencyRange(mSize, mPPI);
    
    %% Fundamental Data Row
    %     fQRows                  = min(max(1, round(mfQ) + [1:10]), size(mtSRF, 1));
    %     [fQMax fQRow]           = max(mtSRF(fQRows, bandMean));
    %     fQRow                   = round(mfQ) + fQRow;
    
    [mCMPPath mCMPExists]   = PatchSeriesProcessor.GetResourcePath('Patch HybridImage', htID, 'png');
    % [mSRFPath mSRFExists]   = PatchSeriesProcessor.GetResourcePath('Patch SRFPlot', htID, 'png');
    % [mPRFPath mPRFExists] 	= PatchSeriesProcessor.GetResourcePath('Patch PRFPlot', htID, 'png');
    
    if forceRenderPatches || ~mCMPExists %&& mSRFExists && mPRFExists)
      %% Composite Images
      [mComp mW mH]         = composePatchImage(htID, scID, ctID, mtID);
            
      %% Composite Plots
      %       % columns: [band fftSum fltSum bandMean bandStd binaryStd]
      %       [mSRFPlots]           = composePatchPlots(mSRFPath, mfQ, fQRow, plotColumn, labelColumns, mW, mH, scSRF, htSRF, ctSRF, mtSRF);
      %       [mPRFPlots]           = composePatchPlots(mPRFPath, mfQ, fQRow, plotColumn, labelColumns, mW, mH, scPRF, htPRF, ctPRF, mtPRF);
      
      %% Save Images
      PatchSeriesProcessor.SaveImage(mComp, 'Patch HybridImage', htID);
      %       PatchSeriesProcessor.SaveImage(mSRFPlots, 'Patch SRFPlot', htID);
      %       PatchSeriesProcessor.SaveImage(mPRFPlots, 'Patch PRFPlot', htID);
      
    end
    
    %     l1 = labelColumns(1);
    %     l2 = labelColumns(2);
    %
    %     lr = fQRow - 1;
    %
    %     % sumTable(m, :)          = [ ... % mRTV mCON mRES mfQ ...
    %     %   scSRF(lr, l1) htSRF(lr, l1) ctSRF(lr, l1) mtSRF(lr, l1) ...
    %     %   scSRF(lr, l2) htSRF(lr, l2) ctSRF(lr, l2) mtSRF(lr, l2) ...
    %     %   scPRF(lr, l1) htPRF(lr, l1) ctPRF(lr, l1) mtPRF(lr, l1) ...
    %     %   scPRF(lr, l2) htPRF(lr, l2) ctPRF(lr, l2) mtPRF(lr, l2) ...
    %     %   ];
    %
    %     srfData                 = [ ...
    %       scSRF(lr, l1) htSRF(lr, l1) ctSRF(lr, l1) mtSRF(lr, l1) ...
    %       scSRF(lr, l2) htSRF(lr, l2) ctSRF(lr, l2) mtSRF(lr, l2) ];
    %
    %     srfSci                  = sciparts(reshape(srfData, [], 1));
    %
    %     prfData                 = [ ...
    %       scPRF(lr, l1) htPRF(lr, l1) ctPRF(lr, l1) mtPRF(lr, l1) ...
    %       scPRF(lr, l2) htPRF(lr, l2) ctPRF(lr, l2) mtPRF(lr, l2) ];
    %
    %     prfSci                  = sciparts(reshape(prfData, [], 1));
    %
    %
    %     outputText{m}           = strtrim(sprintf([ ...
    %       INT     TAB   STR     TAB   STR     TAB   STR     TAB ...
    %       DEC(1)  TAB   DEC(1)  TAB   DEC(3)  TAB   DEC(1)  TAB ...
    %       SCI     TAB   SCI     TAB   SCI     TAB   SCI     TAB ...
    %       SCI     TAB   SCI     TAB   SCI     TAB   SCI     TAB ...
    %       SCI     TAB   SCI     TAB   SCI     TAB   SCI     TAB ...
    %       SCI     TAB   SCI     TAB   SCI     TAB   SCI     TAB ], ...
    %       m, mCMPPath, mSRFPath, mPRFPath, ...
    %       mRTV, mCON, mRES, mfQ, ...
    %       srfSci, prfSci));
    
    catch err
      debugStamp();
      beep;
    end
    
  end
    
  %   sumHeading                =  {strtrim(sprintf('%s\t', ...
  %     'RTV',      'CON',      'RES',      'FQ',...
  %     'S-Avg',    'H-Avg',    'C-Avg',    'M-Avg', ...
  %     'S-Std',    'H-Std',    'C-Std',    'M-Std', ...
  %     'M.S-Avg',  'M.H-Avg',  'M.C-Avg',  'M.M-Avg', ...
  %     'M.S-Std',  'M.H-Std',  'M.C-Std',  'M.M-Std'))};
  %
  %   outHeading                =  {strtrim(sprintf('%s\t', ...
  %     'ID',       'Patch',    'SRF',      'PRF', sumHeading{:}))};
  %
  %
  %   [outPath outExists]       = PatchSeriesProcessor.GetResourcePath('output', 'SummaryTable', 'csv');
  %
  %   if forceOutput || ~outExists
  %     try
  %       cell2csv(outPath, vertcat( outHeading, outputText), '\n');
  %     catch err
  %       debugStamp();
  %       beep;
  %     end
  %   end
  %
  %   [sumPath sumExists]       = PatchSeriesProcessor.GetResourcePath('output', 'SummaryData', 'csv');
  %
  %   if forceOutput || ~sumExists
  %     try
  %       %sumHeading            = outHeading(:, [1 5:end]);
  %       sumText               = outputText(:, [1 5:end]);
  %       cell2csv(sumPath, vertcat( sumHeading, sumText), '\n');
  %     catch err
  %       debugStamp();
  %       beep;
  %     end
  %   end
  %
  
end

function [patchComp W H] = composePatchImage(htID, scID, ctID, mtID)
  
  import Grasppe.ConRes.PatchGenerator.PatchSeriesProcessor; % PatchSeriesProcessor
  
  patchTypes              = {'Halftone', 'Contone'}; % {'Screen', 'Halftone', 'Contone', 'Monotone'};
  patchIDs                = {htID, ctID}; % {scID, htID, ctID, mtID};
  imageGroups             = {'Image', 'RetinaImage'}; % {'Image', 'RetinaImage'; 'FFTImage', 'RetinaFFTImage'};
  
  compDim                 = 1;
  
  imageType               = cell(1, 2);
  img                     = cell(numel(patchTypes), numel(imageGroups));
  
  imgSizes                = [];
  
  N                       = numel(patchTypes);
  P                       = size(imageGroups,2);
  
  %% Load All Images
  for n = 1:N
    for p = 1:P
        img{n, p}         = PatchSeriesProcessor.LoadImage([patchTypes{n} ' ' imageGroups{p}], patchIDs{n});
        r                 = sub2ind([N P], n, p);
        imgSizes(r,:)     = size(img{n, p});
    end
  end
  
  %% Crop All Images
  H                       = min(imgSizes(:,1));
  W                       = min(imgSizes(:,2));
  
  X1                      = 1; %1+floor((imgSizes(:,2) - W) / 2);
  X2                      = W; %X1+W-1;
  
  Y1                      = 1; %1+floor((imgSizes(:,1) - H) / 2);
  Y2                      = H; %Y1+H-1;
  %
  %   for n = 1:N
  %     for p = 1:P
  %         r                 = sub2ind([N P], n, p);
  %         img{n, p}         = img{n, p}(Y1(r):Y2(r), X1(r):X2(r));
  %     end
  %     end
  
  S                         = floor(W/2);  
  patchComp                 = img{1,1}; 
  patchComp(Y1:S,   S+1:X2) = img{2,1}(Y1:S,   S+1:X2);
  patchComp(S+1:Y2, X1:S  ) = img{1,2}(S+1:Y2, X1:S  );
  patchComp(S+1:Y2, S+1:X2) = img{2,2}(S+1:Y2, S+1:X2);
  
  %% Composite Patch Image
  % patchComp               = [];
  %
  % for n = 1:N
  %   groupComp             = [];
  %   for p = 1:P
  %     imageComp           = img{n, p, 1};
  %     S                   = size(imageComp, compDim);
  %     M                   = round(S/2):S;
  %     if compDim==2
  %       imageComp(:,M)    = img{n, p, 2}(:,M);
  %     else
  %       imageComp(M,:)    = img{n, p, 2}(M,:);
  %     end
  %     groupComp           = vertcat(groupComp, imageComp);
  %   end
  %   patchComp             = horzcat(patchComp, groupComp);
  % end
  %
  % W = W * N;
  % H = H * P;
  
end

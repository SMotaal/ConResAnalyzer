classdef Statistics < Grasppe.Core.ValueEnumeration
  enumeration
    %% General Definitions
    ReferenceValue('Statistics.ReferenceValue')
    
    %% Patch Definitions
    % A patch is repeated across sheets. Patch uniformity includes
    % repeatability and does not include evenness. Patches are a
    % spatial subdivision which implies variability on the temporal domain.
    % Since patches are singluar, there can not be variability on the
    % spatial domain.
    %
    
    % Mean of a patch in all sheets (T)
    PatchNorm('Statistics.Patch.Norm')
    
    % Accuracy of a patch against patch norm (T)
    PatchAccuracy('Statistics.Patch.Accuracy')
    
    % Variance of a patch in all sheets (T)
    PatchRepeatability('Statistics.Patch.Repeatability')
    
    
    %% Region Definitions
    % A region contains a number of patches across sheets. Region
    % uniformity includes both evenness and repeatability. Regions are a
    % spatial subdivision which implies variability on the temporal domain.
    % Since regions also include a number of patches, there can also be
    % variability on the spatial domain.
    %
    
    % Mean of all patches in a region in all sheets (S-T)
    RegionNorm('Statistics.Region.Norm')
    
    % Mean of all patches in a region in a single sheet (S)
    RegionMean('Statistics.Region.Mean')
    
    % Accuracy of region mean against region norm (S)
    RegionAccuracy('Statistics.Region.Accuracy')
    
    % Variance of all patches in a region in all sheets (S)
    RegionUniformity('Statistics.Region.Uniformity')
    
    % Variance of all patches in a region in a single sheet (S)
    RegionEvenness('Statistics.Region.Evenness')
    
    % Variance of region mean in one region in all sheets (T)
    RegionRepeatability('Statistics.Region.Repeatability')
    
    %% Sheet Definitions
    % A sheet contains a number regions (1 or more). Sheet uniformity
    % includes evenness and does not include repeatability. Sheets are a
    % temporal subdivision which implies variability on the spatial domain.
    % Since sheets are singluar, there can not be variability on the
    % temporal domain.
    %
    
    % Mean of all pataches in all regions in a single sheet (S)
    SheetMean('Statistics.Sheet.Mean')
    
    % Accuracy of sheet mean against run mean (S)
    SheetAccuracy('Statistics.Sheet.Accuracy')
    
    % Variance of all patches in all regions in a single sheet (S)
    SheetEvenness('Statistics.Sheet.Evenness')
    
    %%% Run Definitions
    % A run contains a number of sheets (more than 1). Run uniformity
    % includes both evenness and repeatability. Runs are the combination of
    % both spatial (region) and temporal (sheet) subdivisions which implies
    % variability on both spatial and temporal domains
    %
    
    % Mean of all patches in all regions in all sheets (S-T)
    RunMean('Statistics.Run.Mean')
    
    % Accuracy of run mean against reference (S-T)
    RunAccuracy('Statistics.Run.Accuracy')
    
    % Variance of all patches in all regions in all sheets (S-T)
    RunUniformity('Statistics.Run.Uniformity')
    
    % Variance of sheet evenness in all sheets (S)
    RunEvenness('Statistics.Run.Evenness')
    
    % Variance of region repeatability in all sheets (T)
    RunRepeatability('Statistics.Run.Repeatability')
    
  end
end


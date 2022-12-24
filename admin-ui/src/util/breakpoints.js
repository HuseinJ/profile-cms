export const BreakpointMinWidth = {
    XS: 0,
    SM: 600,
    MD: 960,
    LG: 1264,
    XL: 1904,
};

export const BreakpointValue = {
    XS: 0,
    SM: 1,
    MD: 2,
    LG: 3,
    XL: 4,
};

export function determineBreakpoint (width) {

    if(width < 0){
        return 0;
    }

    const bpEntries = Object.entries(BreakpointMinWidth);
    const index = bpEntries
        .reverse()
        .findIndex(([, bpWidth]) => bpWidth <= width);
    if (index !== -1) {
        return bpEntries.length - index - 1;
    }
}
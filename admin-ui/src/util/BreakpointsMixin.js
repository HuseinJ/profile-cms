import {BreakpointValue, determineBreakpoint} from './breakpoints';

const Breakpoints = {
    data() {
        return {
            displayBreakpoint: determineBreakpoint(window.innerWidth),
        };
    },
    computed: {
        xlAndDown() {
            return this.displayBreakpoint <= BreakpointValue.XL;
        },
        lgAndDown() {
            return this.displayBreakpoint <= BreakpointValue.LG;
        },
        mdAndDown() {
            return this.displayBreakpoint <= BreakpointValue.MD;
        },
        smAndDown() {
            return this.displayBreakpoint <= BreakpointValue.SM;
        },
        xsAndDown() {
            return this.displayBreakpoint <= BreakpointValue.XS;
        },
        xlAndUp() {
            return this.displayBreakpoint >= BreakpointValue.XL;
        },
        lgAndUp() {
            return this.displayBreakpoint >= BreakpointValue.LG;
        },
        mdAndUp() {
            return this.displayBreakpoint >= BreakpointValue.MD;
        },
        smAndUp() {
            return this.displayBreakpoint >= BreakpointValue.SM;
        },
        xsAndUp() {
            return this.displayBreakpoint >= BreakpointValue.XS;
        },
    },
    created() {
        window.addEventListener('resize', this.windowResize);
    },
    destroyed() {
        window.removeEventListener('resize', this.windowResize);
    },
    methods: {
        windowResize(resizeEvent) {
            this.displayBreakpoint = determineBreakpoint(resizeEvent.currentTarget.innerWidth);
        },
        isMobileDevice() {
            return ( 'ontouchstart' in window ) ||
                ( navigator.maxTouchPoints > 0 );
        },
    },
};

export default Breakpoints;
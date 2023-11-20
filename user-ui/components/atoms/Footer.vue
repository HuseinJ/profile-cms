<script setup lang="ts">

</script>

<template>
<div class="footer">
  <span>© Husein Jusic, 2023</span>
</div>
</template>

<style scoped lang="scss">
@import "@/assets/scss/main.scss"; // Import the SCSS file

 .footer{
   position: fixed;
   left: 0;
   right: 0;
   bottom: 0;
   height: $baseHeight;
   background-color: $primaryDark;

   display: flex;
   align-items: center;
   padding: 0 $baseMargin;

   color: $baseBackground;
 }
</style>
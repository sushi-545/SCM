module.exports = {
    darkMode: 'class', // Enable dark mode
    content: [
        "./src/main/resources/templates/**/*.{html,js}",
        "./src/main/resources/static/css/**/*.{html,js}",
        './node_modules/flowbite/**/*.js'
    ],
    theme: {
        extend: {},
    },
    plugins: [
        require('flowbite/plugin')
    ], safelist: [
        'dark:bg-black', // Ensure safelisted dark-mode classes are added
        'dark:text-white',
        'bg-white',
        'text-black',
    ],
};

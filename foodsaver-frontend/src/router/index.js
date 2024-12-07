import { createRouter, createWebHistory } from "vue-router";

// Import components
const HomeView = () => import('../components/Home.vue');
const FoodItem = () => import('../components/FoodItem.vue');
const RecipeGenerator = () => import('../components/RecipeGenerator.vue');
const SavedRecipes = () => import('../components/SavedRecipes.vue');
const LoginForm = () => import("../components/LoginForm.vue");
const SignUpForm = () => import("../components/SignUpForm.vue");
const UserProfile = () => import('../components/UserProfile.vue');
const WelcomeView = () => import('../components/WelcomeView.vue'); // Optional welcome view
const VerifyPage = () => import('../components/VerifyPage.vue'); // Add the VerifyPage component
const UserNotifications = () => import('../components/UserNotifications.vue');

// Define routes
const routes = [
    { path: '/', redirect: '/home' }, // Redirect root path to /home
    { path: '/home', name: 'Home', component: HomeView },
    { path: '/verify', name: 'VerifyPage', component: VerifyPage }, // Add this route for verification status page
    { path: '/welcome', name: 'WelcomeView', component: WelcomeView, meta: { requiresAuth: true } },
    { path: '/ingredient-list', name: 'IngredientList', component: FoodItem },
    { path: '/recipe-generator', name: 'RecipeGenerator', component: RecipeGenerator },
    { path: '/saved-recipes', name: 'SavedRecipes', component: SavedRecipes, meta: { requiresAuth: true } },
    { path: '/login', name: 'Login', component: LoginForm },
    { path: '/signup', name: 'SignUp', component: SignUpForm },
    { path: '/user-profile', name: 'UserProfile', component: UserProfile, meta: { requiresAuth: true } },
    {path: '/user-notifications', name: 'UserNotifications', component: UserNotifications, meta: {requiresAuth: true} },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});


export default router;
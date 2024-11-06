import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import FoodItem from '../components/FoodItem.vue';
import RecipeGenerator from '../components/RecipeGenerator.vue';
import SavedRecipes from '../components/SavedRecipes.vue';
import LoginForm from "../components/LoginForm.vue";
import SignUpForm from "../components/SignUpForm.vue"; // Importera Sign Up-komponenten
import UserProfile from '../components/Profile.vue'; // Importera den uppdaterade komponenten



const routes = [
    { path: '/', name: 'Home', component: HomeView },
    { path: '/ingredient-list', name: 'IngredientList', component: FoodItem },
    { path: '/recipe-generator', name: 'RecipeGenerator', component: RecipeGenerator },
    { path: '/saved-recipes', name: 'SavedRecipes', component: SavedRecipes },
    { path: '/login', name: 'Login', component: LoginForm },
    { path: '/signup', name: 'SignUp', component: SignUpForm }, // Lägg till Sign Up-rutt
    { path: '/profile', name: 'UserProfile', component: UserProfile }, // Lägg till UserProfile-rutt
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL), // Se till att detta är korrekt
    routes,
});

export default router;
import bikeImg from "../assets/images/bike.jpeg"
import { generatePriceRanges } from "@/utils";

const products = Array(15).fill({
  category: "Mountain bikes",
  img: bikeImg,
  productName: "S-WORKS Enduro",
  owner: { url: "/", name: "Sigma" },
  desc: "Lorem ipsum dolor sit amet consectetur. Et nisl elementum sagittis aliquet enim scelerisque elit.",
  price: 300,
  productUrl: "/"
})

const categories = [
  { id: 1, name: "snowboarding gear" },
  { id: 2, name: "skiing gear" },
  { id: 3, name: "surfing gear" },
  { id: 4, name: "cycling gear" },
  { id: 5, name: "hiking gear" },
  { id: 6, name: "climbing gear" },
  { id: 7, name: "winter sports" },
  { id: 8, name: "water sports" },
  { id: 9, name: "fitness equipment" },
  { id: 10, name: "team sports" }
];

const priceRanges: { start: number; end?: number }[] = [...generatePriceRanges(5, 20, 0), ...generatePriceRanges(1, 50, 1), { start: 150 }]

const subscriptionBenefits = ["Unlimited Listings", "Damage Reporting Tools", "Secure Payments", "Deposit Management"]

export {
  products,
  categories,
  priceRanges,
  subscriptionBenefits,
}

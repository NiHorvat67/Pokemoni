import heroImg from "../assets/images/hero.jpg"
import Search from "../components/Search";
import ProductCard from "../components/ProductCard";
import Calendar23 from "@/components/calendar-23-filter";
import gsap from 'gsap';
import { useGSAP } from '@gsap/react';
import { useEffect, useState } from "react";
import { products as fakeProducts, categories, priceRanges } from "@/constants";
import { useQuery, useMutation } from "@tanstack/react-query";
import axios from "axios";
import { type DateRange } from "react-day-picker"
import * as React from "react"


import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select-filter"


interface PriceRange {
  start: number;
  end?: number;
}

const Homepage = () => {

  const [query, setQuery] = useState("")
  const [categoryId, setCategoryId] = useState("")
  const [priceRange, setPriceRange] = useState<any>({ start: "", end: "" })
  const [dateRange, setDateRange] = useState<any>({ from: "", to: "" })
  const [products, setProducts] = useState(fakeProducts)

  useEffect(() => {
    mutate()
  }, [])

  const { status, error, mutate } = useMutation({
    mutationFn: async () => {
      const params = new URLSearchParams({
        categoryId: categoryId,
        minPrice: priceRange?.start,
        maxPrice: priceRange?.end,
        beginDate: dateRange.from ? dateRange?.from.toISOString().substring(0, 10) : "",
        endDate: dateRange.to ? dateRange?.to.toISOString().substring(0, 10) : "",
      })
      console.log(params.toString())
      return axios
        .get(`/api/advertisement/search?${params.toString()}`)
        .then(res => {
          console.log(res.data)
          return res.data
        })
        .catch(err => console.log(err))
    },
    onSuccess: queryResult => {
      // setProducts(queryResult)
      setProducts(fakeProducts)
    }

  })

  useGSAP(() => {
    gsap.from(".product-card", {
      opacity: 0,
      ease: "power1.inOut",
      y: 25,
      stagger: .06,
      duration: .5
    })
  }, [products])


  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log(query)
    console.log(categoryId)
    console.log(dateRange)
    console.log(priceRange)
    mutate()
  }




  return (

    <section className="padding-x pt-30 sm:pt-50">
      <div className="h-[560px] w-[100vw] absolute top-0 left-0 right-0">
        <img src={heroImg} alt="" className="h-full w-full object-cover object-center" />
        <div className="w-full h-[100.1%] absolute top-0 bg-gradient-to-b from-[#121413]/[0%] from-70% to-dark-bg to-100%"></div>
      </div>

      <section className="max-container relative pt-77">
        <form onSubmit={onSubmit} className="flex flex-col items-center mb-25 sm:mb-40 gap-4 sm:gap-6">
          <Search query={query} setQuery={setQuery} placeholder="Search gear" />
          <div className="flex flex-wrap items-center justify-center gap-4 sm:gap-6 font-inter">

            <Select onValueChange={(value: string) => {
              setCategoryId(value)
            }}>
              <SelectTrigger className="w-[130px] sm:w-[180px]">
                <SelectValue placeholder="Category" />
              </SelectTrigger>
              <SelectContent>
                {categories.map((category) => (
                  <SelectItem key={category.id} value={category.id.toString()}>{category.name}</SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Calendar23 range={dateRange} setRange={setDateRange}></Calendar23>

            <Select onValueChange={(value: string) => {
              const priceRangeParsed = JSON.parse(value)
              setPriceRange(priceRangeParsed)
            }}>
              <SelectTrigger className="w-[130px] sm:w-[180px]">
                <SelectValue placeholder="Price" />
              </SelectTrigger>
              <SelectContent>
                {priceRanges.map((priceRange) => (
                  <SelectItem key={priceRange.start} value={JSON.stringify(priceRange)}>{priceRange.start}€ {priceRange?.end ? ` - ${priceRange?.end}€` : "+"}</SelectItem>
                ))}

              </SelectContent>
            </Select>
          </div>


        </form>

        <section id="#products" className="grid min-[1250px]:grid-cols-4 min-[1000px]:grid-cols-3 min-[700px]:grid-cols-2 grid-cols-1 max-sm:justify-items-center gap-11">
          {products.map((product, index) => (
            <div key={index} className="product-card">

              <ProductCard
                category={product.category}
                img={product.img}
                productName={product.productName}
                owner={product.owner}
                desc={product.desc}
                price={product.price}
                productUrl={product.productUrl}
              ></ProductCard>
            </div>
          ))}
        </section>

      </section>

    </section>

  );
}

export default Homepage;

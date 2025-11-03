import heroImg from "../assets/images/hero.jpg"
import Search from "../components/Search";
import Calendar23 from "@/components/calendar-23-filter";
import { useEffect, useState } from "react";
import { priceRanges } from "@/constants";
import { useQuery, useMutation } from "@tanstack/react-query";
import axios from "axios";
import * as React from "react"
import { checkAppendSearchParams } from "@/utils";
import AdvertisementsGrid from "@/components/AdvertisementsGrid";



import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select-filter"



const Homepage = () => {

  const [query, setQuery] = useState("")
  const [categoryId, setCategoryId] = useState("")
  const [priceRange, setPriceRange] = useState<any>({ start: "", end: "" })
  const [dateRange, setDateRange] = useState<any>({ from: "", to: "" })
  const [products, setProducts] = useState([])

  useEffect(() => {
    mutate()
  }, [categoryId, priceRange, dateRange])


  const { data: categories } = useQuery({
    queryKey: ["categories"],
    queryFn: async () => {
      return axios
        .get("/api/itemtypes/")
        .then(res => {
          console.log(res.data)
          return res.data
        })
        .catch(err => {
          console.log(err)
        })
    }
  })

  const { status, mutate } = useMutation({
    mutationFn: async () => {
      const params = new URLSearchParams({})
      checkAppendSearchParams(params, "itemName", query)
      checkAppendSearchParams(params, "categoryId", categoryId)
      checkAppendSearchParams(params, "minPrice", priceRange?.start)
      checkAppendSearchParams(params, "maxPrice", priceRange?.end)
      checkAppendSearchParams(params, "beginDate", dateRange.from ? dateRange?.from.toISOString().substring(0, 10) : "")
      checkAppendSearchParams(params, "endDate", dateRange.to ? dateRange?.to.toISOString().substring(0, 10) : "")
      return axios
        .get(`/api/advertisements/search?${params.toString()}`)
        .then(res => {
          return res.data
        })
        .catch(err => console.log(err))
    },
    onSuccess: queryResult => {
      setProducts(queryResult)
    }

  })


  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    mutate()
  }




  return (

    <section className="padding-x pt-30 sm:pt-50 pb-120">
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
                {categories && categories.map((category: any) => (
                  <SelectItem key={category.itemtypeId} value={category.itemtypeId.toString()}>{category.itemtypeName}</SelectItem>
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

        {products?.length === 0 && status === "success" &&
          <div className="flex justify-center">
            <h1 className="text-white font-inter font-medium">No advertisements found.</h1>
          </div>
        }
        <AdvertisementsGrid products={products} />

      </section>

    </section>

  );
}

export default Homepage;

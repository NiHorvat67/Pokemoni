"use client"

import { ChevronDownIcon } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Calendar } from "@/components/ui/calendar"
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover"

export default function Calendar23({ range, setRange }: { range: any, setRange: any }) {

  return (
    <div className="flex flex-col gap-3">
      <Popover>
        <PopoverTrigger asChild>
          <Button
            variant="default"
            id="dates"
            className="w-[130px] sm:w-[180px] justify-between font-normal"
          >
            {range?.from && range?.to
              ? `${range.from.toLocaleDateString(undefined, { month: 'numeric', day: 'numeric' })} - ${range.to.toLocaleDateString(undefined, { month: 'numeric', day: 'numeric' })}`
              : "Select date"}
            <ChevronDownIcon />
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-auto overflow-hidden p-0" align="start">
          <Calendar
            mode="range"
            selected={range}
            captionLayout="dropdown"
            onSelect={(range) => {
              setRange(range)
            }}
          />
        </PopoverContent>
      </Popover>
    </div>
  )
}

package logger

import (
	"github.com/fatih/color"
	"time"
)

type log struct {
	Info  func(a ...interface{})
	Error func(a ...interface{})
	Debug func(a ...interface{})
}

var logger = &log{
	Info:  color.New(color.FgGreen).PrintlnFunc(),
	Error: color.New(color.FgRed).PrintlnFunc(),
	Debug: color.New(color.FgYellow).PrintlnFunc(),
}

func Info(msg string) {
	logger.Info(time.Now().Format("02/01/2006 - 15:04:05") + " INFO: " + msg)
}

func Error(msg string) {
	logger.Error(time.Now().Format("02/01/2006 - 15:04:05") + " ERROR: " + msg)
}

func Debug(msg string) {
	logger.Debug(time.Now().Format("02/01/2006 - 15:04:05") + " DEBUG: " + msg)
}
